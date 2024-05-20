package org.silver.services;

import org.silver.models.dtos.playlist.PlaylistBooksDto;
import org.silver.models.dtos.playlist.PlaylistSimpleDto;

import java.util.List;

public interface IPlaylistService {

    List<PlaylistSimpleDto> findByUserId();

    PlaylistBooksDto findByPlaylistId(Long playlistId);

    void savePlaylist(String playlistName);

    void deletePlaylist(Long playlistId);

    void addBook(Long bookId, Long playlistId);

    void removeBook(Long bookId, Long playlistId);

}
