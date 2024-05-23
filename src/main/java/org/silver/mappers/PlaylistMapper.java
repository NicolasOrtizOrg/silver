package org.silver.mappers;

import org.silver.models.dtos.playlist.PlaylistResponseSimpleDTO;
import org.silver.models.entities.BookEntity;
import org.silver.models.entities.PlaylistBookEntity;
import org.silver.models.entities.PlaylistEntity;
import org.silver.models.entities.UserEntity;


public class PlaylistMapper {

    private PlaylistMapper() {
    }

    /**
     * Mapear Entity a SimpleDto
     */
    public static PlaylistResponseSimpleDTO toDto(PlaylistEntity entity) {
        String firstBookImage = entity.getPlaylistBooks().isEmpty() ?
                null : entity.getPlaylistBooks().get(0).getBook().getImage();

        return PlaylistResponseSimpleDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .firstBookImage(firstBookImage)
                .totalBooks(entity.getPlaylistBooks().size())
                .build();
    }

    /**
     * Crear Entity para guardar la Playlist
     */
    public static PlaylistEntity toEntity(String playlistName, Long userId) {
        return PlaylistEntity.builder()
                .name(playlistName)
                .user(UserEntity.builder()
                        .id(userId)
                        .build())
                .build();
    }

    /**
     * Crear Entity para meter un Book dentro dn una Playlist
     */
    public static PlaylistBookEntity toEntity(Long bookId, Long playlistId) {
        return PlaylistBookEntity.builder()
                .book(BookEntity.builder()
                        .id(bookId)
                        .build())
                .playlist(PlaylistEntity.builder()
                        .id(playlistId)
                        .build())
                .build();
    }

}
