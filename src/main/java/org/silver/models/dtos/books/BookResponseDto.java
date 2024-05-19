package org.silver.models.dtos.books;

import lombok.Builder;
import org.silver.models.entities.AuthorEntity;


@Builder
public record BookResponseDto(
        Long id,
        String title,
        String image,
        AuthorEntity author
) {
}
