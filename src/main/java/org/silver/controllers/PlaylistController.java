package org.silver.controllers;

import org.silver.models.dtos.playlist.PlaylistBooksResponseDTO;
import org.silver.models.dtos.playlist.PlaylistCreateDTO;
import org.silver.models.dtos.playlist.PlaylistResponseSimpleDTO;
import org.silver.services.IPlaylistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private final IPlaylistService playlistService;

    public PlaylistController(IPlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public ResponseEntity<List<PlaylistResponseSimpleDTO>> getByUserId() {
        return new ResponseEntity<>(playlistService.findByUserId(), HttpStatus.OK);
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistBooksResponseDTO> getById(@PathVariable Long playlistId) {
        return new ResponseEntity<>(playlistService.findById(playlistId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody PlaylistCreateDTO playlist) {
        playlistService.save(playlist.playlistName());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/add-book")
    public ResponseEntity<Void> addBook(@RequestParam Long bookId,
                                        @RequestParam Long playlistId) {
        playlistService.addBook(bookId, playlistId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/remove-book")
    public ResponseEntity<Void> removeBook(@RequestParam Long bookId,
                                           @RequestParam Long playlistId) {
        playlistService.removeBook(bookId, playlistId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> delete(@PathVariable Long playlistId) {
        playlistService.delete(playlistId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}