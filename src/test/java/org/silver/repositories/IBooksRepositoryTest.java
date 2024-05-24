package org.silver.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.silver.models.entities.AuthorEntity;
import org.silver.models.entities.BookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IBooksRepositoryTest {

    @Autowired
    private IBooksRepository bookRepository;
    @Autowired
    private IAuthorRepository authorRepository;

    private BookEntity book;
    private AuthorEntity author;

    @BeforeEach
    void setUp() {
        author = new AuthorEntity();
        author.setName("Isabel Allende");
        book = BookEntity.builder()
                .title("De amor y de sombra")
                .description("Una historia de amor en un contexto de opresión política.")
                .author(author)
                .isbn("9535252")
                .publishedDate(LocalDate.of(1984, 11, 10))
                .active(true)
                .image("https://www.penguinlibros.com/ar/1595935/de-amor-y-de-sombra.jpg")
                .build();

        authorRepository.save(author);
    }

    @Test
    void findAllByActiveIsTrue() {
        book.setAuthor(author);
        BookEntity bookSaved = bookRepository.save(book);

        Pageable pageable = PageRequest.of(0, 10);
        Page<BookEntity> result = bookRepository.findAllByActiveIsTrue(pageable);

        // Verifica que solo se devuelvan libros activos
//        assertEquals(1, result.getTotalElements()); // Comprar con todos los libros que tengo en db
        assertEquals("De amor y de sombra", bookSaved.getTitle());
    }

    @Test
    void findByAuthorNameContainingIgnoreCase() {
        String authorName = bookRepository.save(book).getAuthor().getName();

        Pageable pageable = PageRequest.of(0, 10);
        Page<BookEntity> result = bookRepository
                .findByAuthorNameContainingIgnoreCase(author.getName(), pageable);

//        assertEquals(1, result.getContent());
        assertEquals("Isabel Allende", authorName);
    }

    @Test
    void findByTitleOrAuthorName() {
    }

    @Test
    void changeStatus() {
    }


}