package org.silver.services.impl;

import org.silver.exceptions.ResourceDuplicate;
import org.silver.exceptions.GenericException;
import org.silver.exceptions.ResourceNotFound;
import org.silver.mappers.BookMapper;
import org.silver.models.dtos.books.BookSaveDTO;
import org.silver.models.dtos.books.BookResponseFullDTO;
import org.silver.models.dtos.books.BookResponseSimpleDTO;
import org.silver.models.entities.AuthorEntity;
import org.silver.models.entities.BookEntity;
import org.silver.repositories.IBooksRepository;
import org.silver.services.IAuthorService;
import org.silver.services.IBookService;
import org.silver.utils.JpaUtils;
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
    private final BookMapper bookMapper;

    public BookServiceImpl(IBooksRepository booksRepository, IAuthorService authorService, BookMapper bookMapper) {
        this.booksRepository = booksRepository;
        this.authorService = authorService;
        this.bookMapper = bookMapper;
    }


    /**
     * Busca lista de Book mediante una Query dinámica gracias a la interfaz Example<>.
     * La Query se genera dependiendo los atributos que se reciban en el DTO.
     *
     * @param dynamicDto: DTO con atributos dinámicos.
     * @return Lista de Book que coincidan con la búsqueda. Con Paginación.
     */
    @Override
    public Page<BookResponseFullDTO> findByDynamicQuery(Example<BookEntity> dynamicDto, Pageable pageable) {
        return booksRepository.findAll(dynamicDto, pageable)
                .map(bookMapper::toDtoFull);
    }

    /**
     * Busca un Book por su ID
     *
     * @param bookId: id del libro.
     * @return Book Dto
     */
    @Override
    public BookResponseFullDTO findById(Long bookId) {
        BookEntity bookDB = booksRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFound(BOOK_NOT_FOUND));
        return bookMapper.toDtoFull(bookDB);
    }

    /**
     * Busca todos los Books que tengan atributo Active=true.
     * Lo devuelve con paginación.
     */
    @Override
    public Page<BookResponseFullDTO> findAllActive(Pageable pageable) {
        return booksRepository.findAllByActiveIsTrue(pageable).map(bookMapper::toDtoFull);
    }

    /**
     * Busca todos los Books por el nombre de su Author.
     * Lo devuelve con paginación.
     */
    @Override
    public Page<BookResponseFullDTO> findByAuthor(String authorName, Pageable pageable) {
        return booksRepository.findByAuthorNameContainingIgnoreCase(authorName, pageable)
                .map(bookMapper::toDtoFull);
    }

    /**
     * Busca todos los Books que en su Title o AuthorName contengan la palabra del parámetro "keyword".
     * Lo devuelve con paginación.
     */
    @Override
    public Page<BookResponseFullDTO> findByKeyword(String keyword, Pageable pageable) {
        return booksRepository.findByTitleOrAuthorName(keyword, pageable)
                .map(bookMapper::toDtoFull);
    }


    /**
     * Guarda un Book.
     * Si el Author NO existe en la base de datos, también se guarda.
     *
     * @param bookDto: DTO del libro a guardar.
     */
    @Transactional
    @Override
    public BookResponseSimpleDTO save(BookSaveDTO bookDto) {
        try {
            // Buscar Author en base de datos si existe, o guardar si no existe
            AuthorEntity authorDB = authorService.getOrSave(bookDto.authorName());

            // Mapear DTO a Entity
            BookEntity bookEntity = bookMapper.toEntity(bookDto);

            bookEntity.setAuthor(authorDB);

            return bookMapper.toDtoSimple(booksRepository.save(bookEntity));
        } catch (DataIntegrityViolationException ex) {
            throw new ResourceDuplicate(ISBN_EXISTS);
        } catch (Exception ex) {
            throw new GenericException(ex.getMessage());
        }
    }

    /**
     * Modifica / Actualiza un Book.
     *
     * @param bookId:  ID del libro a modificar.
     * @param bookDto: DTO del libro con todos sus atributos. No puede contener nulos.
     */
    @Transactional
    @Override
    public void update(Long bookId, BookSaveDTO bookDto) {
        // Obtener Book de base de datos
        Optional<BookEntity> bookDB = booksRepository.findById(bookId);
        if (bookDB.isEmpty()) throw new ResourceNotFound(BOOK_NOT_FOUND);

        // Obtener Author de base de datos si existe, o guardar si no existe
        AuthorEntity authorDB;
        if (bookDto.authorName() == null || bookDto.authorName().isEmpty())
            authorDB = bookDB.get().getAuthor();
        else
            authorDB = authorService.getOrSave(bookDto.authorName());

        // Copiar propiedades no nulas del DTO a la entidad
        JpaUtils.copyNonNullProperties(bookDto, bookDB.get());

        // Asignar campos obtenidos de base de datos
        bookDB.get().setAuthor(authorDB);
        bookDB.get().setId(bookId);

        try {
            booksRepository.save(bookDB.get());
        } catch (Exception ex) {
            throw new GenericException(ex.getMessage());
        }
    }

    /**
     * Soft Delete.
     * Cambia el estado del atributo Active.
     *
     * @param bookId: id del libro.
     * @param status: estado del Book. TRUE/FALSE.
     */
    @Transactional
    @Override
    public void changeStatus(Long bookId, boolean status) {
        if (booksRepository.existsById(bookId))
            booksRepository.changeStatus(bookId, status);
        else
            throw new ResourceNotFound(BOOK_NOT_FOUND);
    }


    private static final String BOOK_NOT_FOUND = "Book not found";
    private static final String ISBN_EXISTS = "ISBN already exists";

}
