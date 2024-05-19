package org.silver.repositories;

import org.silver.models.entities.PlaylistBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPlaylistBookRepository extends JpaRepository<PlaylistBookEntity, Long> {

    Optional<PlaylistBookEntity> findByBookIdAndPlaylistId(Long playlistId, Long bookId);

}
