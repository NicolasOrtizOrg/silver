package org.silver.integration;

import org.silver.models.dtos.books.BookResponseFullDTO;

import java.util.List;

public interface IBookApiService {

    List<BookResponseFullDTO> searchByTitle(String title);

    List<BookResponseFullDTO> searchByAuthor(String author);

}
