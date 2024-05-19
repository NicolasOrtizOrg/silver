package org.silver.repositories;

import org.silver.models.entities.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPlaylistRepository extends JpaRepository<PlaylistEntity, Long> {

    List<PlaylistEntity> findByUserId(Long userId);

}
