package org.silver.integration.google_books;

import org.silver.integration.google_books.models.GoogleBookModel;
import org.silver.integration.google_books.models.ImageLinks;
import org.silver.models.dtos.books.BookResponseFullDTO;
import org.silver.models.entities.AuthorEntity;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

public class GoogleBooksHelper {

    // Validar que las imágenes existen y devuelve la imagen con mayor resolución o null.
    public static String validateImages(ImageLinks images) {
        String defaultImage = "https://fcb-abj-pre.s3.amazonaws.com/img/jugadors/MESSI.jpg";

        if (images == null) return defaultImage;

        // Guardar todos los atributos de la clase
        Field[] fields = images.getClass().getDeclaredFields();

        // Recorrer el array de atrás para adelante para devolver la imagen con mayor resolución.
        // Esto funciona gracias al orden de cómo están definidos los atributos en la clase.
        for (int i = fields.length - 1; i >= 0; i--) {
            try {
                Field field = fields[i]; // Obtener el atributo de la clase.
                field.setAccessible(true); // Hacer accesible.
                Object value = field.get(images);

                if (value != null && !value.toString().isEmpty())
                    return value.toString();

            } catch (Exception ex) {
                return defaultImage;
            }
        }
        return defaultImage;
    }

    public static LocalDate validateDate(String date) {
        if (date == null || date.isEmpty()) return null;

        try {
            return LocalDate.parse(date);
        } catch (Exception ex) {
            String[] dateParts = date.split("-");
            if (dateParts.length == 2) {
                return LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), 1);
            }
            return LocalDate.of(Integer.parseInt(dateParts[0]), 1, 1);
        }
    }


    // Llama a mapear todos los libros y filtra los que tienen valores nulos
    public static List<BookResponseFullDTO> mapToList(List<GoogleBookModel> books) {
        return books.stream()
                .map(GoogleBooksHelper::mapToBookFullDto)
                .filter(book ->
                        book.author() != null
                                && book.title() != null
                )
                .toList();
    }

    // Mapper
    public static BookResponseFullDTO mapToBookFullDto(GoogleBookModel book) {
        try {
            AuthorEntity author = new AuthorEntity();
            LocalDate publishedDate = validateDate(book.getVolumeInfo().getPublishedDate());
            String image = validateImages(book.getVolumeInfo().getImageLinks());

            author.setName(book.getVolumeInfo().getAuthors().get(0));

            return BookResponseFullDTO.builder()
                    .id(-1L) // -1 para poder guardar en base de datos el libro
                    .title(book.getVolumeInfo().getTitle())
                    .description(book.getVolumeInfo().getDescription())
                    .image(image)
                    .isbn(book.getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier())
                    .publishedDate(publishedDate)
                    .author(author)
                    .build();
        } catch (Exception ex) {
            return BookResponseFullDTO.builder().build();
        }
    }


    private GoogleBooksHelper() {
    }
}
