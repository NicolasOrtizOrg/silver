package org.silver.services;

import org.silver.models.dtos.books.BookSaveDTO;
import org.silver.models.dtos.books.BookResponseFullDTO;
import org.silver.models.dtos.books.BookResponseSimpleDTO;
import org.silver.models.entities.BookEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBookService {

    Page<BookResponseFullDTO> findByDynamicQuery(Example<BookEntity> dynamicDto, Pageable pageable);

    BookResponseFullDTO findById(Long bookId);

    Page<BookResponseFullDTO> findAllActive(Pageable pageable);

    Page<BookResponseFullDTO> findByAuthor(String authorName, Pageable pageable);

    // Busca por título o autor que contenga la keyword.
    Page<BookResponseFullDTO> findByKeyword(String keyword, Pageable pageable);

    BookResponseSimpleDTO save(BookSaveDTO bookDto);

    void update(Long bookId, BookSaveDTO bookDto);

    // Soft delete. Cambia el estado "activo".
    void changeStatus(Long bookId, boolean status);

}
