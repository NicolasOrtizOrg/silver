package org.silver.services;

import org.silver.models.dtos.books.BookRequestDto;
import org.silver.models.dtos.books.BookFullDto;
import org.silver.models.entities.BookEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBookService {

    Page<BookFullDto> findByDynamicQuery(Example<BookEntity> example, Pageable pageable);

    BookFullDto findById(Long id);

    Page<BookFullDto> findAllActive(Pageable pageable);

    Page<BookFullDto> findByAuthor(String authorName, Pageable pageable);

    Page<BookFullDto> findByTitleOrAuthorName(String keyword, Pageable pageable);

    void save(BookRequestDto bookDto);

    void update(Long id, BookRequestDto bookDto);

    void changeStatus(Long id, boolean status);

}
