package org.silver.services;

import org.silver.models.dtos.playlist.PlaylistSimpleDto;

import java.util.List;

public interface IPlaylistService {

    List<PlaylistSimpleDto> findByUserId(Long userId);

    PlaylistSimpleDto findByPlaylistId(Long playlistId);

    void savePlaylist(String playlistName, Long userId);

    void deletePlaylist(Long playlistId);

    void addBook(Long bookId, Long playlistId);

    void removeBook(Long bookId, Long playlistId);


}
