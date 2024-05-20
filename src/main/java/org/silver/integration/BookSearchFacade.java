package org.silver.integration;

import org.silver.integration.google_books.GoogleBooksAPI;
import org.silver.models.dtos.books.BookFullDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookSearchFacade {

    private final GoogleBooksAPI googleBooks;

    public BookSearchFacade(GoogleBooksAPI googleBooks) {
        this.googleBooks = googleBooks;
    }

    public List<BookFullDto> searchBooksByTitle(String title) {
        List<BookFullDto> books = new ArrayList<>();

        books.addAll(googleBooks.searchBooks(title));

        return books;
    }

}
