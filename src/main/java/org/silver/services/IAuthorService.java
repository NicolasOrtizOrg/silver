package org.silver.services;

import org.silver.models.entities.AuthorEntity;


public interface IAuthorService {

    void save(String authorName);

    AuthorEntity getOrSave(String authorName);

}
