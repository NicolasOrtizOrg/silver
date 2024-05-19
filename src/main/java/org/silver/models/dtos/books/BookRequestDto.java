package org.silver.models.dtos.books;

import java.time.LocalDate;

public record BookRequestDto(
        String title,
        String description,
        String image,
        String author,
        String isbn,
        LocalDate publishedDate
) {

}
