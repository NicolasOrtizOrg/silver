package org.silver.repositories;

import org.silver.models.entities.PlaylistBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPlaylistBookRepository extends JpaRepository<PlaylistBookEntity, Long> {

    boolean existsByBookIdAndPlaylistId(Long bookId, Long playlistId);

    Optional<PlaylistBookEntity> findByBookIdAndPlaylistId(Long bookId, Long playlistId);

    List<PlaylistBookEntity> findByPlaylistId(Long playlistId);

}
