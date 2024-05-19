package org.silver.models.dtos.books;

/*
* Este DTO se usa para hacer Queries Dinámicas con los atributos definidos
* */
public record BookSearchQuery(
        String title,
        String description,
        String isbn
) {

}
