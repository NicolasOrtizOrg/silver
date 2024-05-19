package org.silver.mappers;

import org.silver.models.dtos.books.BookRequestDto;
import org.silver.models.dtos.books.BookResponseDto;
import org.silver.models.dtos.books.BookSearchQuery;
import org.silver.models.entities.BookEntity;

public class BookMapper {

    private BookMapper() {
    }

    public static BookEntity requestToEntity(BookRequestDto bookDto) {
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

    public static BookResponseDto toDto(BookEntity bookEntity) {
        return BookResponseDto.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .author(bookEntity.getAuthor())
                .build();
    }


}
