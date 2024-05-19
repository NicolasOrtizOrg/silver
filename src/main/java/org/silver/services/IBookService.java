package org.silver.services;

import org.silver.models.dtos.books.BookRequestDto;
import org.silver.models.dtos.books.BookResponseDto;
import org.silver.models.entities.BookEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBookService {

    Page<BookResponseDto> findByDynamicQuery(Example<BookEntity> example, Pageable pageable);

    BookResponseDto findById(Long id);

    Page<BookResponseDto> findAllActive(Pageable pageable);

    Page<BookResponseDto> findByAuthor(String authorName, Pageable pageable);

    Page<BookResponseDto> findByTitleOrAuthorName(String keyword, Pageable pageable);

    void save(BookRequestDto bookDto);

    void update(Long id, BookRequestDto bookDto);

    void changeStatus(Long id, boolean status);

}
