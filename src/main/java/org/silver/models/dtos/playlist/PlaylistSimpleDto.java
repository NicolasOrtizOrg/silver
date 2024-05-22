package org.silver.models.dtos.playlist;

import lombok.Builder;

@Builder
public record PlaylistSimpleDto(
        Long id,
        String name,
        String firstBookImage,
        int totalBooks
) {
}
