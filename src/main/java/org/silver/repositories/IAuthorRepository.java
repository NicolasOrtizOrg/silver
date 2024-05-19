package org.silver.repositories;

import org.silver.models.entities.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAuthorRepository extends JpaRepository<AuthorEntity, Long> {

    Optional<AuthorEntity> findByName(String name);

}
