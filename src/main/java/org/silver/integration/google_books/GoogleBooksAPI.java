package org.silver.integration.google_books;

import org.silver.integration.IBookApiService;
import org.silver.integration.google_books.models.GoogleApiModel;
import org.silver.models.dtos.books.BookResponseFullDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleBooksAPI implements IBookApiService {

    private final RestTemplate restTemplate;

    private static final String URL_BASE = "https://www.googleapis.com/books/v1/volumes";

    public GoogleBooksAPI(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private List<BookResponseFullDTO> searchBooks(URI uri){
        GoogleApiModel response = restTemplate
                .getForObject(uri, GoogleApiModel.class);

        if(response == null)
            return Collections.emptyList();

        return GoogleBooksHelper.mapToList(response.getItems());
    }

    @Override
    public List<BookResponseFullDTO> searchByTitle(String title) {
        URI uriFinal = buildUri("+intitle:", title);
        return searchBooks(uriFinal);
    }

    @Override
    public List<BookResponseFullDTO> searchByAuthor(String author) {
        URI uriFinal = buildUri("+inauthor:", author);
        return searchBooks(uriFinal);
    }


    private URI buildUri(String param, String value){
        return UriComponentsBuilder.fromHttpUrl(URL_BASE)
                .queryParam("q", param.concat(value))
                .queryParam("maxResults", "10")
                .build().toUri();
    }

}
