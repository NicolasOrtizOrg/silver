package org.silver.services.impl;

import org.silver.exceptions.BookNotFoundEx;
import org.silver.exceptions.GenericException;
import org.silver.exceptions.PlaylistNotFoundEx;
import org.silver.exceptions.RelationNotFoundEx;
import org.silver.mappers.PlaylistMapper;
import org.silver.models.dtos.playlist.PlaylistSimpleDto;
import org.silver.models.entities.PlaylistBookEntity;
import org.silver.models.entities.PlaylistEntity;
import org.silver.models.entities.UserEntity;
import org.silver.repositories.IBooksRepository;
import org.silver.repositories.IPlaylistBookRepository;
import org.silver.repositories.IPlaylistRepository;
import org.silver.services.IPlaylistService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistServiceImpl implements IPlaylistService {

    private final IPlaylistRepository playlistRepository;
    private final IPlaylistBookRepository midRepository;
    private final IBooksRepository bookRepository;

    public PlaylistServiceImpl(IPlaylistRepository playlistRepository, IPlaylistBookRepository midRepository, IBooksRepository bookRepository) {
        this.playlistRepository = playlistRepository;
        this.midRepository = midRepository;
        this.bookRepository = bookRepository;
    }


    @Override
    public List<PlaylistSimpleDto> findByUserId(Long userId) {
        return playlistRepository
                .findByUserId(userId)
                .stream()
                .map(PlaylistMapper::toDto)
                .toList();
    }

    @Override
    public PlaylistSimpleDto findByPlaylistId(Long playlistId) {
        PlaylistEntity playlistEntity = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundEx(PLAYLIST_NOT_FOUND));
        return PlaylistMapper.toDto(playlistEntity);
    }

    // Este método debería recibir solo el playlistName, y el userId se obtiene desde el token.
    // Pero no implementé Spring Security ni JWT.
    @Override
    public void savePlaylist(String playlistName, Long userId) {
        // Forma temporal
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);

        PlaylistEntity playlist = new PlaylistEntity();

        playlist.setName(playlistName);
        playlist.setUser(userEntity);

        playlistRepository.save(playlist);
    }

    @Override
    public void deletePlaylist(Long playlistId) {
        if (playlistRepository.existsById(playlistId))
            playlistRepository.deleteById(playlistId);
        else
            throw new PlaylistNotFoundEx(PLAYLIST_NOT_FOUND);
    }

    @Override
    public void addBook(Long bookId, Long playlistId) {
        try {
            PlaylistBookEntity midTable = PlaylistMapper.idsToEntity(bookId, playlistId);
            midRepository.save(midTable);

        } catch (DataIntegrityViolationException ex) {
            if (!bookRepository.existsById(bookId))
                throw new BookNotFoundEx(BOOK_NOT_FOUND);
            if (!playlistRepository.existsById(playlistId))
                throw new PlaylistNotFoundEx(PLAYLIST_NOT_FOUND);
            throw new GenericException(ex.getMessage());
        }
    }

    @Override
    public void removeBook(Long bookId, Long playlistId) {
        PlaylistBookEntity midTable = midRepository.findByBookIdAndPlaylistId(playlistId, bookId)
                .orElseThrow(() -> new RelationNotFoundEx(RELATION_NOT_FOUND));

        midRepository.deleteById(midTable.getId());
    }

    private static final String PLAYLIST_NOT_FOUND = "Playlist no encontrada";
    private static final String BOOK_NOT_FOUND = "Libro no encontrado";
    private static final String RELATION_NOT_FOUND = "El libro no está en la playlist";

}
