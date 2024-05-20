package org.silver.models.dtos.books;

import lombok.Builder;
import org.silver.models.entities.AuthorEntity;


/**
 * DTO simple
 * */
@Builder
public record BookSimpleDto(
        Long id,
        String title,
        String image,
        AuthorEntity author
) {
}
