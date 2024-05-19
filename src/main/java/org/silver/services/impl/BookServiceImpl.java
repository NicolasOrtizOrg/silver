package org.silver.services.impl;

import org.modelmapper.ModelMapper;
import org.silver.exceptions.BookExistsEx;
import org.silver.exceptions.BookNotFoundEx;
import org.silver.exceptions.GenericException;
import org.silver.mappers.BookMapper;
import org.silver.models.dtos.BookRequest;
import org.silver.models.dtos.BookResponse;
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


@Service
public class BookServiceImpl implements IBookService {

    private final IBooksRepository booksRepository;
    private final IAuthorService authorService;
    private final ModelMapper modelMapper;

    public BookServiceImpl(IBooksRepository booksRepository, IAuthorService authorService, ModelMapper modelMapper) {
        this.booksRepository = booksRepository;
        this.authorService = authorService;
        this.modelMapper = modelMapper;
    }


    @Override
    public BookResponse findById(Long id) {
        BookEntity bookDB = booksRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundEx(BOOK_NOT_FOUND));
        return BookMapper.toDto(bookDB);
    }

    @Override
    public Page<BookResponse> findAllActive(Pageable pageable) {
        return booksRepository.findAllByActiveIsTrue(pageable).map(BookMapper::toDto);
    }

    @Override
    public Page<BookResponse> findByAuthor(String authorName, Pageable pageable) {
        return booksRepository.findByAuthorName(authorName, pageable)
                .map(BookMapper::toDto);
    }

    @Override
    public Page<BookResponse> findByTitleOrAuthorName(String keyword, Pageable pageable) {
        return booksRepository.findByTitleOrAuthorName(keyword, pageable)
                .map(BookMapper::toDto);
    }

    @Transactional
    @Override
    public void save(BookRequest bookDto) {
        try {
            AuthorEntity authorDB = authorService.getOrSave(bookDto.author());

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
    public void update(Long id, BookRequest bookDto) {
        BookEntity bookDB = booksRepository.findById(id)
                .orElseThrow(()-> new BookNotFoundEx(BOOK_NOT_FOUND));

        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(bookDto, bookDB);

        try {
            AuthorEntity authorDB = authorService.getOrSave(bookDto.author());
            bookDB.setAuthor(authorDB);

            booksRepository.save(bookDB);
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

    @Override
    public Page<BookResponse> findByDynamicQuery(Example<BookEntity> example, Pageable pageable) {
        return booksRepository.findAll(example, pageable)
                .map(BookMapper::toDto);
    }

    private static final String BOOK_NOT_FOUND = "Book not found";
    private static final String ISBN_EXISTS = "ISBN already exists";

}
