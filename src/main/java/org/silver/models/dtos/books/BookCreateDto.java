package org.silver.models.dtos.books;

import java.time.LocalDate;

public record BookCreateDto(
        String title,
        String description,
        String image,
        String isbn,
        LocalDate publishedDate,
        String authorName
) {

}
