package org.silver.integration;

import org.silver.models.dtos.books.BookFullDto;

import java.util.List;

public interface IBookApiService {

    List<BookFullDto> searchBooks(String title);

}
