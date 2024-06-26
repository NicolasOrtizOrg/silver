package org.silver.repositories;

import org.silver.models.entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface IBooksRepository extends JpaRepository<BookEntity, Long> {

    Page<BookEntity> findAllByActiveIsTrue(Pageable pageable);

    Page<BookEntity> findByAuthorNameContainingIgnoreCase(String authorName, Pageable pageable);

    @Query("SELECT b FROM BookEntity b WHERE b.title LIKE %:keyword% OR b.author.name LIKE %:keyword%")
    Page<BookEntity> findByTitleOrAuthorName(@Param("keyword") String keyword, Pageable pageable);

    @Modifying
    @Query("UPDATE BookEntity b SET b.active = :status WHERE b.id = :bookId")
    void changeStatus(@Param("bookId") Long bookId,
                      @Param("status") boolean status);

}
