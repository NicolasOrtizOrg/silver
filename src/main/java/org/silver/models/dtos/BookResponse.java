package org.silver.models.dtos;

import lombok.Builder;
import org.silver.models.entities.AuthorEntity;

import java.time.LocalDate;

@Builder
public record BookResponse(
        Long id,
        String title,
        String description,
        AuthorEntity author,
        String isbn,
        LocalDate publishedDate,
        boolean isActive
) {
}
