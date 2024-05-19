package org.silver.repositories;

import org.silver.models.entities.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPlaylistRepository extends JpaRepository<PlaylistEntity, Long> {

    List<PlaylistEntity> findByUserId(Long userId);

}
