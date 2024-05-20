package org.silver.controllers;

import org.silver.mappers.BookMapper;
import org.silver.models.dtos.books.BookCreateDto;
import org.silver.models.dtos.books.BookSearchQuery;
import org.silver.models.dtos.books.BookFullDto;
import org.silver.models.entities.BookEntity;
import org.silver.services.IBookService;
import org.silver.utils.PaginationUtils;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/books")
public class BooksController {

    private final IBookService bookService;

    public BooksController(IBookService bookService) {
        this.bookService = bookService;
    }


    /** Eliminar campos vacíos en las requests */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }


    /**
     * Buscar todos los libros activos.
     * @param page: número de página.
     * @return lista de Books con paginación.
     * */
    @GetMapping
    public ResponseEntity<Page<BookFullDto>> getAllActive(@RequestParam int page) {
        Pageable pageable = PaginationUtils.setPagination(page);
        return new ResponseEntity<>(bookService.findAllActive(pageable), HttpStatus.OK);
    }


    /**
     * Buscar Book por su ID.
     * @param bookId: ID del libro.
     * @return Book.
     * */
    @GetMapping("/filter/{bookId}")
    public ResponseEntity<BookFullDto> getByBookId(@PathVariable Long bookId) {
        return new ResponseEntity<>(bookService.findById(bookId), HttpStatus.OK);
    }


    /**
     * Buscar todos los Book que contengan la palabra clave en su Title o AuthorName.
     * @param keyword: palabra clave.
     * @param page: número de página.
     * @return lista de libros con paginación.
     * */
    @GetMapping("/filter")
    public ResponseEntity<Page<BookFullDto>> getByTitleOrAuthorName(@RequestParam String keyword,
                                                                    @RequestParam int page) {
        Pageable pageable = PaginationUtils.setPagination(page);
        return new ResponseEntity<>(bookService.findByTitleOrAuthorName(keyword, pageable), HttpStatus.OK);
    }


    /**
     * Buscar todos los Book de un Author por su nombre.
     * @param authorName: nombre del Author.
     * @param page: número de página.
     * @return lista de libros con paginación.
     * */
    @GetMapping("/filter/author")
    public ResponseEntity<Page<BookFullDto>> getByAuthorName(@RequestParam String authorName,
                                                             @RequestParam int page) {
        Pageable pageable = PaginationUtils.setPagination(page);
        return new ResponseEntity<>(bookService.findByAuthor(authorName, pageable), HttpStatus.OK);
    }


    /**
     * Buscar Books mediante Queries dinámicas a través de los atributos obtenidos en el DTO.
     * @param bookRequest: DTO de Book con atributos que coincidan con la búsqueda.
     * @param page: número de página.
     * @return lista de libros con paginación.
     * */
    @GetMapping("/filter/q")
    public ResponseEntity<Page<BookFullDto>> getByDynamicQuery(BookSearchQuery bookRequest,
                                                               @RequestParam int page) {
        // Mapear DTO a entidad
        BookEntity bookEntity = BookMapper.toEntityFromQuery(bookRequest);

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
     * @param bookCreateDto: DTO del Book.
     * */
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody BookCreateDto bookCreateDto) {
        bookService.save(bookCreateDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     * Modificar/Actualizar un Book.
     * @param bookId: ID del libro a modificar.
     * @param bookCreateDto: DTO del Book.
     * */
    @PutMapping("/{bookId}")
    public ResponseEntity<Void> update(@PathVariable Long bookId,
                                       @RequestBody BookCreateDto bookCreateDto) {
        bookService.update(bookId, bookCreateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Soft Delete. Modificar estado de un Book.
     * @param bookId: ID del libro a modificar.
     * @param status: estado del Book. TRUE/FALSE.
     * */
    @PatchMapping("/{bookId}/{status}")
    public ResponseEntity<Void> changeStatus(@PathVariable Long bookId,
                                             @PathVariable boolean status) {
        bookService.changeStatus(bookId, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
