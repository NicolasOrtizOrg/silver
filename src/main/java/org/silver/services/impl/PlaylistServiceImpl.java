package org.silver.services.impl;

import org.silver.exceptions.*;
import org.silver.mappers.BookMapper;
import org.silver.mappers.PlaylistMapper;
import org.silver.models.dtos.books.BookResponseSimpleDTO;
import org.silver.models.dtos.playlist.PlaylistBooksResponseDTO;
import org.silver.models.dtos.playlist.PlaylistResponseSimpleDTO;
import org.silver.models.entities.PlaylistBookEntity;
import org.silver.models.entities.PlaylistEntity;
import org.silver.repositories.IBooksRepository;
import org.silver.repositories.IPlaylistBookRepository;
import org.silver.repositories.IPlaylistRepository;
import org.silver.services.IPlaylistService;
import org.silver.utils.HeaderUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistServiceImpl implements IPlaylistService {

    private final IPlaylistRepository playlistRepository;
    private final IPlaylistBookRepository midRepository;
    private final IBooksRepository bookRepository;
    private final BookMapper bookMapper;
    private final PlaylistMapper playlistMapper;

    public PlaylistServiceImpl(IPlaylistRepository playlistRepository, IPlaylistBookRepository midRepository, IBooksRepository bookRepository, BookMapper bookMapper, PlaylistMapper playlistMapper) {
        this.playlistRepository = playlistRepository;
        this.midRepository = midRepository;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.playlistMapper = playlistMapper;
    }


    /**
     * Busca una lista de Playlist de un User.
     * El userId lo obtiene desde los headers de la petición.
     *
     * @return lista de playlists de un usuario.
     */
    @Override
    public List<PlaylistResponseSimpleDTO> findByUserId() {
        Long userId = Long.valueOf(HeaderUtils.getHeader("userId"));

        List<PlaylistEntity> playlist = playlistRepository.findByUserId(userId);

        return playlist.stream()
                .map(playlistMapper::toDto)
                .toList();
    }

    /**
     * Busca una Playlist por su ID.
     *
     * @param playlistId: ID de la playlist.
     * @return Playlist con sus libros.
     */
    @Override
    public PlaylistBooksResponseDTO findById(Long playlistId) {

        // Obtener playlist
        PlaylistEntity playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFound(PLAYLIST_NOT_FOUND));

        // Obtener libros de la playlist
        List<BookResponseSimpleDTO> books = midRepository.findByPlaylistId(playlistId)
                .stream()
                .map(book -> bookMapper.toDtoSimple(book.getBook()))
                .toList();

        return new PlaylistBooksResponseDTO(playlistId, playlist.getName(), books);
    }

    /**
     * Guardar Playlist
     *
     * @param playlistName: nombre de la playlist.
     */
    @Override
    public void save(String playlistName) {
        Long userId = Long.valueOf(HeaderUtils.getHeader("userId"));
        try {
            PlaylistEntity playlist = playlistMapper.toEntity(playlistName, userId);

            playlistRepository.save(playlist);
        } catch (DataIntegrityViolationException ex) {
            throw new GenericException(USERID_NOT_FOUND);
        }
    }

    /**
     * Elimina una Playlist por su ID.
     *
     * @param playlistId: ID de la Playlist a eliminar.
     */
    @Override
    public void delete(Long playlistId) {
        if (playlistRepository.existsById(playlistId))
            playlistRepository.deleteById(playlistId);
        else
            throw new ResourceNotFound(PLAYLIST_NOT_FOUND);
    }

    /**
     * Agrega un Book dentro de una Playlist.
     *
     * @param bookId:     ID del Book.
     * @param playlistId: ID de la Playlist.
     */
    @Override
    public void addBook(Long bookId, Long playlistId) {
        if (midRepository.existsByBookIdAndPlaylistId(bookId, playlistId))
            throw new GenericException(RELATION_EXISTS);

        try {
            PlaylistBookEntity midTable = playlistMapper.toEntity(bookId, playlistId);
            midRepository.save(midTable);

        } catch (DataIntegrityViolationException ex) {
            if (!bookRepository.existsById(bookId))
                throw new ResourceNotFound(BOOK_NOT_FOUND);
            if (!playlistRepository.existsById(playlistId))
                throw new ResourceNotFound(PLAYLIST_NOT_FOUND);
        }
    }

    /**
     * Quita un Book de una Playlist.
     *
     * @param bookId:     ID del Book.
     * @param playlistId: ID de la Playlist.
     */
    @Override
    public void removeBook(Long bookId, Long playlistId) {
        PlaylistBookEntity midTable = midRepository.findByBookIdAndPlaylistId(bookId, playlistId)
                .orElseThrow(() -> new ResourceNotFound(RELATION_NOT_FOUND));

        midRepository.deleteById(midTable.getId());
    }


    private static final String USERID_NOT_FOUND = "El userId no existe";
    private static final String PLAYLIST_NOT_FOUND = "Playlist no encontrada";
    private static final String BOOK_NOT_FOUND = "Libro no encontrado";
    private static final String RELATION_NOT_FOUND = "El libro no está en la playlist";
    private static final String RELATION_EXISTS = "Ya tenés guardado ese libro en la playlist";

}
