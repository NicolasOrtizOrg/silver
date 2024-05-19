package org.silver.models.dtos;

import java.time.LocalDate;

public record BookRequest(
        String title,
        String description,
        String image,
        String author,
        String isbn,
        LocalDate publishedDate
) {

}
