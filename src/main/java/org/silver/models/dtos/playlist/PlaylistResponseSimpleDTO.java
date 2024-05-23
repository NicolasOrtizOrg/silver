package org.silver.models.dtos.playlist;

import lombok.Builder;

@Builder
public record PlaylistResponseSimpleDTO(
        Long id,
        String name,
        String firstBookImage,
        int totalBooks
) {
}
