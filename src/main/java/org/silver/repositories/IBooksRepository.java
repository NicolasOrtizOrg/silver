package org.silver.repositories;

import org.silver.models.entities.BookEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface IBooksRepository extends JpaRepository<BookEntity, Long> {

    Page<BookEntity> findByAuthorName(String authorName, Pageable pageable);

    Page<BookEntity> findByCategoryName(String categoryName, Pageable pageable);

    @Query("SELECT b FROM BookEntity b WHERE b.title LIKE %:keyword%" +
            " OR b.author.name LIKE %:keyword%")
    Page<BookEntity> findByTitleOrAuthorName(@Param("keyword") String keyword, Pageable pageable);

    Page<BookEntity> findByTitleContainsIgnoreCase(String categoryName, Pageable pageable);

    @Modifying
    @Query("UPDATE BookEntity b SET b.isActive = :status WHERE b.id = :bookId")
    void changeStatus(@Param("bookId") Long bookId,
                      @Param("status") boolean status);

}
