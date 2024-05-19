package org.silver.mappers;

import org.silver.models.dtos.playlist.PlaylistSimpleDto;
import org.silver.models.entities.BookEntity;
import org.silver.models.entities.PlaylistBookEntity;
import org.silver.models.entities.PlaylistEntity;
import org.silver.models.entities.UserEntity;


public class PlaylistMapper {

    private PlaylistMapper() {
    }

    public static PlaylistSimpleDto toDto(PlaylistEntity entity){
        return PlaylistSimpleDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static PlaylistBookEntity idsToEntity(Long bookId, Long playlistId){
        BookEntity book = new BookEntity();
        book.setId(bookId);
        PlaylistEntity playlist = new PlaylistEntity();
        playlist.setId(playlistId);

        return PlaylistBookEntity.builder()
                .book(book)
                .playlist(playlist)
                .build();
    }

    public static PlaylistEntity nameToEntity(String playlistName, Long userId){
        PlaylistEntity playlist = new PlaylistEntity();
        UserEntity userEntity = new UserEntity();

        userEntity.setId(userId);

        playlist.setName(playlistName);
        playlist.setUser(userEntity);

        return playlist;
    }

}
