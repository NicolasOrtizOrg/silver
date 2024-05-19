package org.silver.controllers;

import org.silver.mappers.BookMapper;
import org.silver.models.dtos.books.BookRequestDto;
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


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }


    @GetMapping
    public ResponseEntity<Page<BookFullDto>> getAllActive(@RequestParam int page) {
        Pageable pageable = PaginationUtils.setPagination(page);
        return new ResponseEntity<>(bookService.findAllActive(pageable), HttpStatus.OK);
    }


    @GetMapping("/filter/{bookId}")
    public ResponseEntity<BookFullDto> getByBookId(@PathVariable Long bookId) {
        return new ResponseEntity<>(bookService.findById(bookId), HttpStatus.OK);
    }


    @GetMapping("/filter")
    public ResponseEntity<Page<BookFullDto>> getByTitleOrAuthorName(@RequestParam String keyword,
                                                                    @RequestParam int page) {
        Pageable pageable = PaginationUtils.setPagination(page);
        return new ResponseEntity<>(bookService.findByTitleOrAuthorName(keyword, pageable), HttpStatus.OK);
    }


    @GetMapping("/filter/author")
    public ResponseEntity<Page<BookFullDto>> getByAuthorName(@RequestParam String authorName,
                                                             @RequestParam int page) {
        Pageable pageable = PaginationUtils.setPagination(page);
        return new ResponseEntity<>(bookService.findByAuthor(authorName, pageable), HttpStatus.OK);
    }


    @GetMapping("/filter/q")
    public ResponseEntity<Page<BookFullDto>> getByDynamicQuery(BookSearchQuery bookRequest,
                                                               @RequestParam int page) {
        BookEntity bookEntity = BookMapper.queryToEntity(bookRequest);

        // Configuración de Paginación
        Pageable pageable = PaginationUtils.setPagination(page);

        // Configuración de Queries dinámicas
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<BookEntity> example = Example.of(bookEntity, matcher);

        return new ResponseEntity<>(bookService.findByDynamicQuery(example, pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody BookRequestDto bookRequestDto) {
        bookService.save(bookRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PutMapping("/{bookId}")
    public ResponseEntity<Void> update(@PathVariable Long bookId,
                                       @RequestBody BookRequestDto bookRequestDto) {
        bookService.update(bookId, bookRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PatchMapping("/{bookId}/{status}")
    public ResponseEntity<Void> changeStatus(@PathVariable Long bookId,
                                             @PathVariable boolean status) {
        bookService.changeStatus(bookId, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
