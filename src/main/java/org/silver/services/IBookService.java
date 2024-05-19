package org.silver.services;

import org.silver.models.dtos.BookRequest;
import org.silver.models.dtos.BookResponse;
import org.silver.models.entities.BookEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBookService {

    BookResponse findById(Long id);

    Page<BookResponse> findAll(Pageable pageable);

    Page<BookResponse> findByTitleOrAuthor(String keyword, Pageable pageable);

    Page<BookResponse> findByAuthor(String authorName, Pageable pageable);

    Page<BookResponse> findByTitle(String title, Pageable pageable);

    Page<BookResponse> findByCategory(String categoryName, Pageable pageable);

    void save(BookRequest bookDto);

    void update(Long id, BookRequest bookDto);

    void changeStatus(Long id, boolean status);


    Page<BookResponse> findByQuery(Example<BookEntity> example, Pageable pageable);

}
