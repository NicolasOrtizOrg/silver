package org.silver.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.silver.models.entities.AuthorEntity;
import org.silver.repositories.IAuthorRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @InjectMocks
    private AuthorServiceImpl authorService;
    @Mock
    private IAuthorRepository authorRepository;

    private AuthorEntity author;
    private String authorName; // Nombre con .trim()

    @BeforeEach
    void setup(){
        author = new AuthorEntity();
        author.setName("Julio Cortázar");
        authorName = author.getName().trim();
    }


    @Test
    void save() {
        when(authorRepository.save(any(AuthorEntity.class))).thenReturn(author);

        authorService.save(authorName);

        verify(authorRepository, times(1)).save(any(AuthorEntity.class));
    }

    @Test
    void getOrSave_AuthorExists() {
        when(authorRepository.findByName(authorName))
                .thenReturn(Optional.of(author));

        AuthorEntity authorDB = authorService.getOrSave(authorName);
        assertNotNull(authorDB);
        assertEquals(authorName, authorDB.getName());

        verify(authorRepository, times(1)).findByName(authorName);
        verify(authorRepository, times(0)).save(any(AuthorEntity.class));
    }

    @Test
    void getOrSave_AuthorNotExists() {
        when(authorRepository.findByName(authorName))
                .thenReturn(Optional.empty());
        when(authorRepository.save(any(AuthorEntity.class)))
                .thenReturn(author);

        AuthorEntity authorDB = authorService.getOrSave(authorName);
        assertNotNull(authorDB);
        assertEquals(authorName, authorDB.getName());

        verify(authorRepository, times(1)).findByName(authorName);
        verify(authorRepository, times(1)).save(any(AuthorEntity.class));
    }
}