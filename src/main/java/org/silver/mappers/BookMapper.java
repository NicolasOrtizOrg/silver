package org.silver.mappers;

import org.silver.models.dtos.books.BookCreateDto;
import org.silver.models.dtos.books.BookFullDto;
import org.silver.models.dtos.books.BookSearchQuery;
import org.silver.models.dtos.books.BookSimpleDto;
import org.silver.models.entities.AuthorEntity;
import org.silver.models.entities.BookEntity;


public class BookMapper {

    private BookMapper() {
    }

    public static BookEntity requestToEntity(BookCreateDto bookDto) {
        return BookEntity.builder()
                .title(bookDto.title())
                .description(bookDto.description())
                .image(bookDto.image())
                .isbn(bookDto.isbn())
                .publishedDate(bookDto.publishedDate())
                .active(true)
                .build();
    }

    public static BookEntity queryToEntity(BookSearchQuery bookDto) {
        return BookEntity.builder()
                .title(bookDto.title())
                .description(bookDto.description())
                .isbn(bookDto.isbn())
                .active(true)
                .build();
    }

    public static BookFullDto toFullDto(BookEntity bookEntity) {
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

    public static BookSimpleDto toSimpleDto(BookEntity bookEntity) {
        return BookSimpleDto.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .author(bookEntity.getAuthor())
                .image(bookEntity.getImage())
                .build();
    }

    public static BookEntity toUpdate(BookCreateDto source, BookEntity destination){
        AuthorEntity author = new AuthorEntity();
        author.setName(source.authorName());

        return BookEntity.builder()
                .id(destination.getId())
                .title(source.title())
                .description(source.description())
                .image(source.image())
                .isbn(source.isbn())
                .publishedDate(source.publishedDate())
//                .author(author)
                .build();
    }


}
