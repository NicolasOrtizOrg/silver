package org.silver.mappers;

import org.silver.models.dtos.BookRequest;
import org.silver.models.dtos.BookResponse;
import org.silver.models.dtos.BookSearchQuery;
import org.silver.models.entities.BookEntity;

public class BookMapper {

    private BookMapper() {
    }

    public static BookEntity requestToEntity(BookRequest bookDto) {
        return BookEntity.builder()
                .title(bookDto.title())
                .description(bookDto.description())
                .isbn(bookDto.isbn())
                .publishedDate(bookDto.publishedDate())
                .isActive(true)
                .build();
    }

    public static BookEntity queryToEntity(BookSearchQuery bookDto) {
        return BookEntity.builder()
                .title(bookDto.title())
                .description(bookDto.description())
                .isbn(bookDto.isbn())
                .isActive(true)
                .build();
    }

    public static BookResponse toDto(BookEntity bookEntity) {
        return BookResponse.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .description(bookEntity.getDescription())
                .isbn(bookEntity.getIsbn())
                .publishedDate(bookEntity.getPublishedDate())
                .isActive(true)
                .author(bookEntity.getAuthor())
                .build();
    }


}
