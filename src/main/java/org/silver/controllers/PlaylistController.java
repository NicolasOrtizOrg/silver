package org.silver.controllers;

import org.silver.models.dtos.playlist.PlaylistBooksDto;
import org.silver.models.dtos.playlist.PlaylistSimpleDto;
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
    public ResponseEntity<List<PlaylistSimpleDto>> getPlaylistsByUserId() {
        return new ResponseEntity<>(playlistService.findByUserId(), HttpStatus.OK);
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistBooksDto> getPlaylistById(@PathVariable Long playlistId) {
        return new ResponseEntity<>(playlistService.findByPlaylistId(playlistId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> savePlaylist(@RequestParam String playlistName) {
        playlistService.savePlaylist(playlistName);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/add-book")
    public ResponseEntity<Void> addBookInPlaylist(@RequestParam Long bookId,
                                                  @RequestParam Long playlistId) {
        playlistService.addBook(bookId, playlistId);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @DeleteMapping("/remove-book")
    public ResponseEntity<Void> removeBookFromPlaylist(@RequestParam Long bookId,
                                                       @RequestParam Long playlistId) {
        playlistService.removeBook(bookId, playlistId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long playlistId) {
        playlistService.deletePlaylist(playlistId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}