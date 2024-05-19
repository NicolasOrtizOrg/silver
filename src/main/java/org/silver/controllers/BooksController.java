package org.silver.controllers;

import org.modelmapper.ModelMapper;
import org.silver.mappers.BookMapper;
import org.silver.models.dtos.BookSearchQuery;
import org.silver.models.dtos.BookResponse;
import org.silver.models.entities.BookEntity;
import org.silver.services.IBookService;
import org.silver.utils.PaginationConfig;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Page<BookResponse>> getAllBooks(@RequestParam int page) {
        Pageable pageable = PaginationConfig.setPagination(page);

        return new ResponseEntity<>(bookService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<BookResponse>> getByKeyword(@RequestParam String keyword,
                                                           @RequestParam int page) {
        Pageable pageable = PaginationConfig.setPagination(page);

        return new ResponseEntity<>(bookService.findByTitleOrAuthor(keyword, pageable), HttpStatus.OK);
    }

    @GetMapping("/filter/category")
    public ResponseEntity<Page<BookResponse>> getByCategory(@RequestParam String category,
                                                            @RequestParam int page) {
        Pageable pageable = PaginationConfig.setPagination(page);

        return new ResponseEntity<>(bookService.findByCategory(category, pageable), HttpStatus.OK);
    }

//    @GetMapping("/filter/q")
//    public ResponseEntity<Page<BookResponse>> getByCategory(@RequestParam String category,
//                                                            @RequestParam int page) {
//        Pageable pageable = PaginationConfig.setPagination(page);
//
//        return new ResponseEntity<>(bookService.findByCategory(category, pageable), HttpStatus.OK);
//    }

    @GetMapping("/filter/example")
    public ResponseEntity<Page<BookResponse>> getByExample(BookSearchQuery bookRequest,
                                                           @RequestParam int page) {

        BookEntity bookEntity = BookMapper.queryToEntity(bookRequest);

        Pageable pageable = PaginationConfig.setPagination(page);

        // Configuración de Queries dinámicas
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<BookEntity> example = Example.of(bookEntity, matcher);

        return new ResponseEntity<>(bookService.findByQuery(example, pageable), HttpStatus.OK);
    }


}
