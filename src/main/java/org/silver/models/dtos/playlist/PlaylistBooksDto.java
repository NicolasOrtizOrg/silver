package org.silver.models.dtos.playlist;

import lombok.Builder;
import org.silver.models.dtos.books.BookSimpleDto;

import java.util.List;

@Builder
public record PlaylistBooksDto(
        Long id,
        String name,
        List<BookSimpleDto> books
) {
}
