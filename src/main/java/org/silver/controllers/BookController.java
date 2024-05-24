package org.silver.controllers;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.silver.integration.BookSearchFacade;
import org.silver.mappers.BookMapper;
import org.silver.models.dtos.books.BookSaveDTO;
import org.silver.models.dtos.books.BookSearchQueryDTO;
import org.silver.models.dtos.books.BookResponseFullDTO;
import org.silver.models.dtos.books.BookResponseSimpleDTO;
import org.silver.models.entities.BookEntity;
import org.silver.services.IBookService;
import org.silver.utils.PaginationUtils;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;


@Log4j2
@RestController
@RequestMapping("/books")
public class BookController {

    private final IBookService bookService;
    private final BookSearchFacade bookSearchFacade;
    private final BookMapper bookMapper;

    public BookController(IBookService bookService, BookSearchFacade bookSearchFacade, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookSearchFacade = bookSearchFacade;
        this.bookMapper = bookMapper;
    }


    /**
     * Eliminar campos vacíos en las requests
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }


    /**
     * Buscar todos los libros activos.
     *
     * @param page: número de página.
     * @return lista de Books con paginación.
     */
    @GetMapping
    public ResponseEntity<Page<BookResponseFullDTO>> getAllActive(@RequestParam int page) {
        Pageable pageable = PaginationUtils.setPagination(page);
        return new ResponseEntity<>(bookService.findAllActive(pageable), HttpStatus.OK);
    }


    /**
     * Buscar Book por su ID.
     *
     * @param bookId: id del libro.
     * @return Book.
     */
    @GetMapping("/filter/{bookId}")
    public ResponseEntity<BookResponseFullDTO> getByBookId(@PathVariable Long bookId) {
        return new ResponseEntity<>(bookService.findById(bookId), HttpStatus.OK);
    }


    /**
     * Buscar todos los Book que contengan la palabra clave en su Title o AuthorName.
     *
     * @param keyword: palabra clave.
     * @param page:    número de página.
     * @return lista de libros con paginación.
     */
    @GetMapping("/filter")
    public ResponseEntity<Page<BookResponseFullDTO>> getByKeyword(@RequestParam String keyword,
                                                                  @RequestParam int page) {
        Pageable pageable = PaginationUtils.setPagination(page);

        Page<BookResponseFullDTO> books = bookService.findByKeyword(keyword, pageable);

        // Verificar si nuestra base de datos tiene los libros buscados.
        // Si no existen, los va a buscar en la API externa.
        if (books.getContent().isEmpty()) {
            books = new PageImpl<>(bookSearchFacade.searchBooksByTitle(keyword));
        }

        return new ResponseEntity<>(books, HttpStatus.OK);
    }


    /**
     * Buscar todos los Book de un Author por su nombre.
     *
     * @param authorName: nombre del Author.
     * @param page:       número de página.
     * @return lista de libros con paginación.
     */
    @GetMapping("/filter/author")
    public ResponseEntity<Page<BookResponseFullDTO>> getByAuthorName(@RequestParam String authorName,
                                                                     @RequestParam int page) {
        Pageable pageable = PaginationUtils.setPagination(page);

        Page<BookResponseFullDTO> books = bookService.findByAuthor(authorName, pageable);

        // Verificar si nuestra base de datos tiene los libros buscados.
        // Si no existen, los va a buscar en la API externa.
        if (books.getContent().isEmpty()) {
            books = new PageImpl<>(bookSearchFacade.searchBooksByAuthor(authorName));
        }

        return new ResponseEntity<>(books, HttpStatus.OK);
    }


    /**
     * Buscar Books mediante Queries dinámicas a través de los atributos obtenidos en el DTO.
     *
     * @param bookRequest: DTO de Book con atributos que coincidan con la búsqueda.
     * @param page:        número de página.
     * @return lista de libros con paginación.
     */
    @GetMapping("/filter/q")
    public ResponseEntity<Page<BookResponseFullDTO>> getByDynamicQuery(BookSearchQueryDTO bookRequest,
                                                                       @RequestParam int page) {

        // Mapear DTO a entidad
        BookEntity bookEntity = bookMapper.toEntity(bookRequest);

        // Configuración de Paginación
        Pageable pageable = PaginationUtils.setPagination(page);

        // Configuración de Queries dinámicas
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<BookEntity> example = Example.of(bookEntity, matcher);

        return new ResponseEntity<>(bookService.findByDynamicQuery(example, pageable), HttpStatus.OK);
    }

    /**
     * Guardar un Book.
     *
     * @param bookSaveDto: DTO del Book.
     */
    @PostMapping
    public ResponseEntity<BookResponseSimpleDTO> save(@Valid @RequestBody BookSaveDTO bookSaveDto) {
        log.info("Intentando guardar Book{}", bookSaveDto);
        return new ResponseEntity<>(bookService.save(bookSaveDto), HttpStatus.CREATED);
    }


    /**
     * Modificar/Actualizar un Book.
     *
     * @param bookId:      ID del libro a modificar.
     * @param bookSaveDto: DTO del Book.
     */
    @PutMapping("/{bookId}")
    public ResponseEntity<Void> update(@PathVariable Long bookId,
                                       @Valid @RequestBody BookSaveDTO bookSaveDto) {
        log.info("Modificando Book con ID: {}. Nuevo Book: {}", bookId, bookSaveDto);
        bookService.update(bookId, bookSaveDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Soft Delete. Modificar estado de un Book.
     *
     * @param bookId: ID del libro a modificar.
     * @param status: estado del Book. TRUE/FALSE.
     */
    @PatchMapping("/{bookId}/{status}")
    public ResponseEntity<Void> changeStatus(@PathVariable Long bookId,
                                             @PathVariable boolean status) {
        log.info("Cambiando estado al Book: {}. Estado: {}", bookId, status);
        bookService.changeStatus(bookId, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
