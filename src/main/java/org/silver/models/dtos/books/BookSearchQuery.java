package org.silver.models.dtos.books;

/**
 * DTO para hacer Queries dinámicas dependiendo los atributos recibidos.
 * Para agregar atributos, también deben agregarse en el ExampleMatcher.
 */
public record BookSearchQuery(
        String title,
        String description,
        String isbn
) {

}
