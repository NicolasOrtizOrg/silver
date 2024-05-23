package org.silver.services;

import org.silver.models.dtos.playlist.PlaylistBooksResponseDTO;
import org.silver.models.dtos.playlist.PlaylistResponseSimpleDTO;

import java.util.List;

public interface IPlaylistService {

    List<PlaylistResponseSimpleDTO> findByUserId();

    PlaylistBooksResponseDTO findById(Long playlistId);

    void save(String playlistName);

    void delete(Long playlistId);

    void addBook(Long bookId, Long playlistId);

    void removeBook(Long bookId, Long playlistId);

}
