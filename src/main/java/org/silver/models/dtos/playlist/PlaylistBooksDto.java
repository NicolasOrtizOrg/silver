package org.silver.models.dtos.playlist;

import org.silver.models.dtos.books.BookSimpleDto;

import java.util.List;

public record PlaylistBooksDto(
        Long id,
        String name,
        List<BookSimpleDto> books
) {
}
