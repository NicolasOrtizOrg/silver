package org.silver.services.impl;

import org.silver.exceptions.BookExistsEx;
import org.silver.exceptions.BookNotFoundEx;
import org.silver.exceptions.GenericException;
import org.silver.mappers.BookMapper;
import org.silver.models.dtos.books.BookCreateDto;
import org.silver.models.dtos.books.BookFullDto;
import org.silver.models.entities.AuthorEntity;
import org.silver.models.entities.BookEntity;
import org.silver.repositories.IBooksRepository;
import org.silver.services.IAuthorService;
import org.silver.services.IBookService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class BookServiceImpl implements IBookService {

    private final IBooksRepository booksRepository;
    private final IAuthorService authorService;

    public BookServiceImpl(IBooksRepository booksRepository, IAuthorService authorService) {
        this.booksRepository = booksRepository;
        this.authorService = authorService;
    }


    @Override
    public Page<BookFullDto> findByDynamicQuery(Example<BookEntity> example, Pageable pageable) {
        return booksRepository.findAll(example, pageable)
                .map(BookMapper::toFullDto);
    }

    @Override
    public BookFullDto findById(Long id) {
        BookEntity bookDB = booksRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundEx(BOOK_NOT_FOUND));
        return BookMapper.toFullDto(bookDB);
    }

    @Override
    public Page<BookFullDto> findAllActive(Pageable pageable) {
        return booksRepository.findAllByActiveIsTrue(pageable).map(BookMapper::toFullDto);
    }

    @Override
    public Page<BookFullDto> findByAuthor(String authorName, Pageable pageable) {
        return booksRepository.findByAuthorNameContainingIgnoreCase(authorName, pageable)
                .map(BookMapper::toFullDto);
    }

    @Override
    public Page<BookFullDto> findByTitleOrAuthorName(String keyword, Pageable pageable) {
        return booksRepository.findByTitleOrAuthorName(keyword, pageable)
                .map(BookMapper::toFullDto);
    }


    @Transactional
    @Override
    public void save(BookCreateDto bookDto) {
        try {
            AuthorEntity authorDB = authorService.getOrSave(bookDto.authorName());

            BookEntity bookEntity = BookMapper.requestToEntity(bookDto);
            bookEntity.setAuthor(authorDB);

            booksRepository.save(bookEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new BookExistsEx(ISBN_EXISTS);
        } catch (Exception ex) {
            throw new GenericException(ex.getMessage());
        }
    }

    @Transactional
    @Override
    public void update(Long id, BookCreateDto bookDto) {
        // Obtener Book de base de datos
        Optional<BookEntity> bookDB = booksRepository.findById(id);
        if (bookDB.isEmpty())
            throw new BookNotFoundEx(BOOK_NOT_FOUND);

        // Obtener Author de base de datos si existe, o guardar si no existe
        AuthorEntity authorDB = authorService.getOrSave(bookDto.authorName());

        // Mapear de DTO a Entidad
        BookEntity updatedBook = BookMapper.requestToEntity(bookDto);

        // Asignar campos obtenidos de base de datos
        updatedBook.setAuthor(authorDB);
        updatedBook.setId(id);

        try {
            booksRepository.save(updatedBook);
        } catch (DataIntegrityViolationException ex) {
            throw new BookExistsEx(ISBN_EXISTS);
        } catch (Exception ex) {
            throw new GenericException(ex.getMessage());
        }
    }

    @Transactional
    @Override
    public void changeStatus(Long id, boolean status) {
        if (booksRepository.existsById(id))
            booksRepository.changeStatus(id, status);
        else
            throw new BookNotFoundEx(BOOK_NOT_FOUND);
    }


    private static final String BOOK_NOT_FOUND = "Book not found";
    private static final String ISBN_EXISTS = "ISBN already exists";

}
