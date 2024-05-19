package org.silver.models.dtos.books;


public record BookSearchQuery(
        String title,
        String description,
        String isbn
) {

}
