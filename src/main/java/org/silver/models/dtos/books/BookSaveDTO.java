package org.silver.models.dtos.books;

import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

/**
 * DTO para CREAR y ACTUALIZAR Books
 * */
@Builder
public record BookSaveDTO(
        @Size(min = 2, max = 255, message = "El título deber tener entre 2 y 255 caracteres")
        String title,
        String description,
        String image,
        String isbn,
        LocalDate publishedDate,
        String authorName
) {

}
