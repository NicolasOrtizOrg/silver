package org.silver.mappers;

import org.silver.models.dtos.books.BookCreateDto;
import org.silver.models.dtos.books.BookFullDto;
import org.silver.models.dtos.books.BookSearchQuery;
import org.silver.models.dtos.books.BookSimpleDto;
import org.silver.models.entities.BookEntity;


public class BookMapper {

    private BookMapper() {
    }

    /**
     * Mapea los DTO de creación/actualización a una entidad.
     * */
    public static BookEntity toEntityFromRequest(BookCreateDto bookDto) {
        return BookEntity.builder()
                .title(bookDto.title())
                .description(bookDto.description())
                .image(bookDto.image())
                .isbn(bookDto.isbn())
                .publishedDate(bookDto.publishedDate())
                .active(true)
                .build();
    }

    /**
     * Mapea los DTO de Queries Dinámicas a una entidad.
     * */
    public static BookEntity toEntityFromQuery(BookSearchQuery bookDto) {
        return BookEntity.builder()
                .title(bookDto.title())
                .description(bookDto.description())
                .isbn(bookDto.isbn())
                .active(true)
                .build();
    }

    /**
     * Mapea una entidad a un DTO full.
     * */
    public static BookFullDto toFullDtoFromEntity(BookEntity bookEntity) {
        return BookFullDto.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .description(bookEntity.getDescription())
                .image(bookEntity.getImage())
                .isbn(bookEntity.getIsbn())
                .publishedDate(bookEntity.getPublishedDate())
                .author(bookEntity.getAuthor())
                .build();
    }

    /**
     * Mapea una entidad a un DTO Simple.
     * */
    public static BookSimpleDto toSimpleDtoFromEntity(BookEntity bookEntity) {
        return BookSimpleDto.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .image(bookEntity.getImage())
                .author(bookEntity.getAuthor())
                .build();
    }

}
