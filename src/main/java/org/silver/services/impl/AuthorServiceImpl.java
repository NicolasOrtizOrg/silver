package org.silver.services.impl;

import org.silver.models.entities.AuthorEntity;
import org.silver.repositories.IAuthorRepository;
import org.silver.services.IAuthorService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorServiceImpl implements IAuthorService {

    private final IAuthorRepository authorRepository;

    public AuthorServiceImpl(IAuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    /**
     * Crea un Author.
     * Los nombres se pueden repetir.
     *
     * @param authorName: nombre del Author
     */
    @Override
    public void save(String authorName) {
        AuthorEntity author = new AuthorEntity();
        author.setName(authorName);
        authorRepository.save(author);
    }

    /**
     * Obtener o crear un Author.
     *
     * @param authorName: nombre del Author.
     * @return AuthorEntity: entidad desde base de datos.
     */
    @Override
    public AuthorEntity getOrSave(String authorName) {
        String name = authorName.trim();

        Optional<AuthorEntity> authorDB = authorRepository.findByName(name);

        if (authorDB.isPresent()) {
            return authorDB.get();
        } else {
            AuthorEntity author = new AuthorEntity();
            author.setName(name);
            return authorRepository.save(author);
        }
    }
}
