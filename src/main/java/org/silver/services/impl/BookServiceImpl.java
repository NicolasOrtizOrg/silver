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


    /**
     * Busca lista de Book mediante una Query dinámica gracias a la interfaz Example<>.
     * La Query se genera dependiendo los atributos que se reciban en el DTO.
     * @param dynamicDto: DTO con atributos dinámicos.
     * @return Lista de Book que coincidan con la búsqueda. Con Paginación.
     * */
    @Override
    public Page<BookFullDto> findByDynamicQuery(Example<BookEntity> dynamicDto, Pageable pageable) {
        return booksRepository.findAll(dynamicDto, pageable)
                .map(BookMapper::toFullDtoFromEntity);
    }

    /**
     * Busca un Book por su ID
     * @param bookId: ID del libro.
     * @return Book Dto
     * */
    @Override
    public BookFullDto findById(Long bookId) {
        BookEntity bookDB = booksRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundEx(BOOK_NOT_FOUND));
        return BookMapper.toFullDtoFromEntity(bookDB);
    }

    /**
     * Busca todos los Books que tengan atributo Active=true.
     * Lo devuelve con paginación.
     * */
    @Override
    public Page<BookFullDto> findAllActive(Pageable pageable) {
        return booksRepository.findAllByActiveIsTrue(pageable).map(BookMapper::toFullDtoFromEntity);
    }

    /**
     * Busca todos los Books por el nombre de su Author.
     * Lo devuelve con paginación.
     * */
    @Override
    public Page<BookFullDto> findByAuthor(String authorName, Pageable pageable) {
        return booksRepository.findByAuthorNameContainingIgnoreCase(authorName, pageable)
                .map(BookMapper::toFullDtoFromEntity);
    }

    /**
     * Busca todos los Books que en su Title o AuthorName contengan la palabra del parámetro "keyword".
     * Lo devuelve con paginación.
     * */
    @Override
    public Page<BookFullDto> findByTitleOrAuthorName(String keyword, Pageable pageable) {
        return booksRepository.findByTitleOrAuthorName(keyword, pageable)
                .map(BookMapper::toFullDtoFromEntity);
    }


    /**
     * Guarda un Book.
     * Si el Author NO existe en la base de datos, también se guarda.
     * @param bookDto: DTO del libro a guardar.
     * */
    @Transactional
    @Override
    public void save(BookCreateDto bookDto) {
        try {
            // Buscar Author en base de datos si existe, o guardar si no existe
            AuthorEntity authorDB = authorService.getOrSave(bookDto.authorName());

            // Mapear DTO a Entity
            BookEntity bookEntity = BookMapper.toEntityFromRequest(bookDto);

            bookEntity.setAuthor(authorDB);

            booksRepository.save(bookEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new BookExistsEx(ISBN_EXISTS);
        } catch (Exception ex) {
            throw new GenericException(ex.getMessage());
        }
    }

    /**
     * Modifica / Actualiza un Book.
     * Para modificar de manera dinámica, usar @DynamicUpdate y mi clase JpaUtils
     * @param bookId: ID del libro a modificar.
     * @param bookDto: DTO del libro con todos sus atributos. No puede contener nulos.
     * */
    @Transactional
    @Override
    public void update(Long bookId, BookCreateDto bookDto) {
        // Obtener Book de base de datos
        Optional<BookEntity> bookDB = booksRepository.findById(bookId);
        if (bookDB.isEmpty())
            throw new BookNotFoundEx(BOOK_NOT_FOUND);

        // Obtener Author de base de datos si existe, o guardar si no existe
        AuthorEntity authorDB = authorService.getOrSave(bookDto.authorName());

        // Mapear de DTO a Entidad
        BookEntity updatedBook = BookMapper.toEntityFromRequest(bookDto);

        // Asignar campos obtenidos de base de datos
        updatedBook.setAuthor(authorDB);
        updatedBook.setId(bookId);

        try {
            booksRepository.save(updatedBook);
        } catch (DataIntegrityViolationException ex) {
            throw new BookExistsEx(ISBN_EXISTS);
        } catch (Exception ex) {
            throw new GenericException(ex.getMessage());
        }
    }

    /**
     * Soft Delete.
     * Cambia el estado del atributo Active.
     * @param bookId: ID del libro.
     * @param status: estado del Book. TRUE/FALSE.
     * */
    @Transactional
    @Override
    public void changeStatus(Long bookId, boolean status) {
        if (booksRepository.existsById(bookId))
            booksRepository.changeStatus(bookId, status);
        else
            throw new BookNotFoundEx(BOOK_NOT_FOUND);
    }


    private static final String BOOK_NOT_FOUND = "Book not found";
    private static final String ISBN_EXISTS = "ISBN already exists";

}
