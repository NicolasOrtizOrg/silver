package org.silver.seeders;

import org.silver.models.entities.AuthorEntity;
import org.silver.models.entities.BookEntity;
import org.silver.repositories.IAuthorRepository;
import org.silver.repositories.IBooksRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Cargar Books al iniciar el proyecto
 * */
@Component
public class BookSeeder {

    private final IBooksRepository booksRepository;
    private final IAuthorRepository authorRepository;

    public BookSeeder(IBooksRepository booksRepository, IAuthorRepository authorRepository) {
        this.booksRepository = booksRepository;
        this.authorRepository = authorRepository;
    }

    public void loadData() {
        if (booksRepository.count() == 0) {

            AuthorEntity author1 = authorRepository.save(new AuthorEntity(1L, "Gabriel García Márquez"));
            AuthorEntity author2 = authorRepository.save(new AuthorEntity(2L, "Isabel Allende"));
            AuthorEntity author3 = authorRepository.save(new AuthorEntity(3L, "Mario Vargas Llosa"));

            List<BookEntity> books = new ArrayList<>();

            books.add(BookEntity.builder()
                    .title("Cien años de soledad")
                    .description("La historia de la familia Buendía en el pueblo ficticio de Macondo.")
                    .author(author1)
                    .isbn("9780307474728")
                    .publishedDate(LocalDate.of(1967, 5, 30))
                    .active(true)
                    .image("https://example.com/images/cien_anos_de_soledad.jpg")
                    .build());

            books.add(BookEntity.builder()
                    .title("El amor en los tiempos del cólera")
                    .description("Una historia de amor que abarca más de cincuenta años.")
                    .author(author1)
                    .isbn("9780307389732")
                    .publishedDate(LocalDate.of(1985, 10, 5))
                    .active(true)
                    .image("https://example.com/images/el_amor_en_los_tiempos_del_colera.jpg")
                    .build());

            books.add(BookEntity.builder()
                    .title("La casa de los espíritus")
                    .description("La saga de la familia Trueba a lo largo de varias generaciones.")
                    .author(author2)
                    .isbn("9780553383804")
                    .publishedDate(LocalDate.of(1982, 4, 12))
                    .active(true)
                    .image("https://example.com/images/la_casa_de_los_espiritus.jpg")
                    .build());

            books.add(BookEntity.builder()
                    .title("De amor y de sombra")
                    .description("Una historia de amor en un contexto de opresión política.")
                    .author(author2)
                    .isbn("9780374530536")
                    .publishedDate(LocalDate.of(1984, 11, 10))
                    .active(true)
                    .image("https://example.com/images/de_amor_y_de_sombra.jpg")
                    .build());

            books.add(BookEntity.builder()
                    .title("La ciudad y los perros")
                    .description("La vida en un colegio militar de Lima.")
                    .author(author3)
                    .isbn("9780060732790")
                    .publishedDate(LocalDate.of(1963, 10, 13))
                    .active(true)
                    .image("https://example.com/images/la_ciudad_y_los_perros.jpg")
                    .build());

            books.add(BookEntity.builder()
                    .title("La fiesta del chivo")
                    .description("La dictadura de Rafael Leónidas Trujillo en República Dominicana.")
                    .author(author3)
                    .isbn("9780312420277")
                    .publishedDate(LocalDate.of(2000, 5, 30))
                    .active(true)
                    .image("https://example.com/images/la_fiesta_del_chivo.jpg")
                    .build());

            books.add(BookEntity.builder()
                    .title("Crónica de una muerte anunciada")
                    .description("La investigación de un asesinato en un pequeño pueblo.")
                    .author(author1)
                    .isbn("9780307387349")
                    .publishedDate(LocalDate.of(1981, 12, 2))
                    .active(true)
                    .image("https://example.com/images/cronica_de_una_muerte_anunciada.jpg")
                    .build());

            books.add(BookEntity.builder()
                    .title("Paula")
                    .description("Un homenaje a la hija fallecida de Isabel Allende.")
                    .author(author2)
                    .isbn("9780060927219")
                    .publishedDate(LocalDate.of(1994, 5, 20))
                    .active(true)
                    .image("https://example.com/images/paula.jpg")
                    .build());

            books.add(BookEntity.builder()
                    .title("Pantaleón y las visitadoras")
                    .description("La historia de un oficial del ejército y su misión secreta en la selva peruana.")
                    .author(author3)
                    .isbn("9780312421892")
                    .publishedDate(LocalDate.of(1973, 8, 1))
                    .active(true)
                    .image("https://example.com/images/pantaleon_y_las_visitadoras.jpg")
                    .build());

            books.add(BookEntity.builder()
                    .title("El otoño del patriarca")
                    .description("Una novela sobre un dictador caribeño ficticio.")
                    .author(author1)
                    .isbn("9780060882860")
                    .publishedDate(LocalDate.of(1975, 6, 25))
                    .active(true)
                    .image("https://example.com/images/el_otono_del_patriarca.jpg")
                    .build());

            booksRepository.saveAll(books);
        }
    }

}
