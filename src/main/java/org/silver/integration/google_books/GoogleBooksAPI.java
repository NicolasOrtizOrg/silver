package org.silver.integration.google_books;

import org.silver.integration.IBookApiService;
import org.silver.models.dtos.books.BookFullDto;
import org.silver.models.entities.AuthorEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * todo: verificar que se pongan bien las fechas
 * todo: verificar si las images son null
 * todo: verificar cantidad de libros por API, poner maximo
 * */
@Service
public class GoogleBooksAPI implements IBookApiService {

    private final RestTemplate restTemplate;

    private static final String URL_BASE = "https://www.googleapis.com/books/v1/volumes";

    public GoogleBooksAPI(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<BookFullDto> searchBooks(String title) {
        URI urlFinal = UriComponentsBuilder.fromHttpUrl(URL_BASE)
                .queryParam("q", "+intitle:".concat(title))
                .queryParam("maxResults", "10")
                .build()
                .toUri();

        GoogleApiModel response = restTemplate
                .getForObject(urlFinal, GoogleApiModel.class);

        if (response == null)
            return Collections.emptyList();

        return mapToList(response.getItems());
    }

    private List<BookFullDto> mapToList(List<GoogleBookModel> books) {
        return books.stream()
                .map(this::toBookFullDto)
                .toList();
    }

    private BookFullDto toBookFullDto(GoogleBookModel book) {
        AuthorEntity author = new AuthorEntity();
        author.setName(book.getVolumeInfo().getAuthors().get(0));

        return BookFullDto.builder()
                .title(book.getVolumeInfo().getTitle())
                .description(book.getVolumeInfo().getDescription())
                .image("book.getVolumeInfo().getImageLinks().getSmallThumbnail()")
                .isbn(book.getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier())
                .publishedDate(LocalDate.now()) // Fecha falsa
                .author(author)
                .build();
    }

}
