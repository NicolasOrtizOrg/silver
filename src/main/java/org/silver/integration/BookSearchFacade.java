package org.silver.integration;

import org.silver.integration.google_books.GoogleBooksAPI;
import org.silver.models.dtos.books.BookResponseFullDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookSearchFacade {

    private final GoogleBooksAPI googleBooks;
    List<BookResponseFullDTO> books = new ArrayList<>();

    public BookSearchFacade(GoogleBooksAPI googleBooks) {
        this.googleBooks = googleBooks;
    }


    public List<BookResponseFullDTO> searchBooksByTitle(String title) {
        // Agregar todas las API que integremos
        books.addAll(googleBooks.searchByTitle(title));

        return books;
    }

    public List<BookResponseFullDTO> searchBooksByAuthor(String author) {
        // Agregar todas las API que integremos
        books.addAll(googleBooks.searchByAuthor(author));

        return books;
    }

}
