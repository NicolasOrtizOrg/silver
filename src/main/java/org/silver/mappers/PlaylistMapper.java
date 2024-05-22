package org.silver.mappers;

import org.silver.models.dtos.books.BookSimpleDto;
import org.silver.models.dtos.playlist.PlaylistBooksDto;
import org.silver.models.dtos.playlist.PlaylistSimpleDto;
import org.silver.models.entities.BookEntity;
import org.silver.models.entities.PlaylistBookEntity;
import org.silver.models.entities.PlaylistEntity;
import org.silver.models.entities.UserEntity;

import java.util.ArrayList;
import java.util.List;


public class PlaylistMapper {

    private PlaylistMapper() {
    }

    /**
     * Mapear Entity a FullDto.
     * */
    public static PlaylistBooksDto toFullDtoFromEntity(PlaylistEntity entity){
        List<BookSimpleDto> booksDto = new ArrayList<>();

         entity.getPlaylistBooks()
                 .forEach(book -> booksDto.add(BookMapper.toSimpleDtoFromEntity(book.getBook())));

        return PlaylistBooksDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .books(booksDto)
                .build();
    }

    /**
     * Mapear Entity a SimpleDto
     * */
    public static PlaylistSimpleDto toSimpleDtoFromEntity(PlaylistEntity entity){
        return PlaylistSimpleDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .firstBookImage(entity.getPlaylistBooks().get(0).getBook().getImage())
                .build();
    }

    /**
     * Crear Entity para guardar la Playlist
     * */
    public static PlaylistEntity toEntity(String playlistName, Long userId){
        return PlaylistEntity.builder()
                .name(playlistName)
                .user(UserEntity.builder()
                        .id(userId)
                        .build())
                .build();
    }

    /**
     * Crear Entity para meter un Book dentro dn una Playlist
     * */
    public static PlaylistBookEntity toEntity(Long bookId, Long playlistId){
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
