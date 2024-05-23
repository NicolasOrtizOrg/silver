package org.silver.mappers;

import org.silver.models.dtos.books.BookSaveDTO;
import org.silver.models.dtos.books.BookResponseFullDTO;
import org.silver.models.dtos.books.BookSearchQueryDTO;
import org.silver.models.dtos.books.BookResponseSimpleDTO;
import org.silver.models.entities.BookEntity;
import org.springframework.stereotype.Component;


@Component
public class BookMapper {


    /**
     * Mapea los DTO de creación/actualización a una entidad.
     * */
    public BookEntity toEntity(BookSaveDTO bookDto) {
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
    public BookEntity toEntity(BookSearchQueryDTO bookDto) {
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
    public BookResponseFullDTO toDtoFull(BookEntity bookEntity) {
        return BookResponseFullDTO.builder()
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
    public BookResponseSimpleDTO toDtoSimple(BookEntity bookEntity) {
        return BookResponseSimpleDTO.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .image(bookEntity.getImage())
                .author(bookEntity.getAuthor())
                .build();
    }

}
