package org.silver.models.dtos;


public record BookSearchQuery(
        String title,
        String description,
        String isbn
) {

}
