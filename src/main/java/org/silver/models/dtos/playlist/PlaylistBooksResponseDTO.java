package org.silver.models.dtos.playlist;

import lombok.Builder;
import org.silver.models.dtos.books.BookResponseSimpleDTO;

import java.util.List;

@Builder
public record PlaylistBooksResponseDTO(
        Long id,
        String name,
        List<BookResponseSimpleDTO> books
) {
}
