package org.silver.models.dtos.books;

import lombok.Builder;
import org.silver.models.entities.AuthorEntity;

import java.time.LocalDate;


/**
 * DTO con todos los datos completos
 * */
@Builder
public record BookFullDto(
        Long id,
        String title,
        String description,
        String image,
        String isbn,
        LocalDate publishedDate,
        AuthorEntity author
) {
}
