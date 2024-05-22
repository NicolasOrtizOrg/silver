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
                    .image("https://images.cdn3.buscalibre.com/fit-in/360x360/61/8d/618d227e8967274cd9589a549adff52d.jpg")
                    .build());

            books.add(BookEntity.builder()
                    .title("El amor en los tiempos del cólera")
                    .description("Una historia de amor que abarca más de cincuenta años.")
                    .author(author1)
                    .isbn("9780307389732")
                    .publishedDate(LocalDate.of(1985, 10, 5))
                    .active(true)
                    .image("https://centrogabo.org/sites/default/files/Imagenes/el_amor_en_los_tiempos_del_colera_0.jpg")
                    .build());

            books.add(BookEntity.builder()
                    .title("La casa de los espíritus")
                    .description("La saga de la familia Trueba a lo largo de varias generaciones.")
                    .author(author2)
                    .isbn("9780553383804")
                    .publishedDate(LocalDate.of(1982, 4, 12))
                    .active(true)
                    .image("https://www.tematika.com/media/catalog/Ilhsa/Imagenes/706758.jpg")
                    .build());

            books.add(BookEntity.builder()
                    .title("De amor y de sombra")
                    .description("Una historia de amor en un contexto de opresión política.")
                    .author(author2)
                    .isbn("9780374530536")
                    .publishedDate(LocalDate.of(1984, 11, 10))
                    .active(true)
                    .image("https://www.penguinlibros.com/ar/1595935/de-amor-y-de-sombra.jpg")
                    .build());

            books.add(BookEntity.builder()
                    .title("La ciudad y los perros")
                    .description("La vida en un colegio militar de Lima.")
                    .author(author3)
                    .isbn("9780060732790")
                    .publishedDate(LocalDate.of(1963, 10, 13))
                    .active(true)
                    .image("https://2.blogs.elcomercio.pe/huellasdigitales/wp-content/uploads/sites/137/2015/07/LCYP-PORTADA.jpg")
                    .build());

            books.add(BookEntity.builder()
                    .title("La fiesta del chivo")
                    .description("La dictadura de Rafael Leónidas Trujillo en República Dominicana.")
                    .author(author3)
                    .isbn("9780312420277")
                    .publishedDate(LocalDate.of(2000, 5, 30))
                    .active(true)
                    .image("https://www.teatroinfantaisabel.es/wp-content/uploads/2019/10/LFDC_Qwantiq_400x600.png")
                    .build());

            books.add(BookEntity.builder()
                    .title("Crónica de una muerte anunciada")
                    .description("La investigación de un asesinato en un pequeño pueblo.")
                    .author(author1)
                    .isbn("9780307387349")
                    .publishedDate(LocalDate.of(1981, 12, 2))
                    .active(true)
                    .image("https://beta-content.tap-commerce.com.ar//cover/original/9789500751674_1.jpg?id_com=1174")
                    .build());

            books.add(BookEntity.builder()
                    .title("Paula")
                    .description("Un homenaje a la hija fallecida de Isabel Allende.")
                    .author(author2)
                    .isbn("9780060927219")
                    .publishedDate(LocalDate.of(1994, 5, 20))
                    .active(true)
                    .image("https://m.media-amazon.com/images/I/91HuTO-ESzL._AC_UF1000,1000_QL80_DpWeblab_.jpg")
                    .build());

            books.add(BookEntity.builder()
                    .title("Pantaleón y las visitadoras")
                    .description("La historia de un oficial del ejército y su misión secreta en la selva peruana.")
                    .author(author3)
                    .isbn("9780312421892")
                    .publishedDate(LocalDate.of(1973, 8, 1))
                    .active(true)
                    .image("https://pics.filmaffinity.com/Pantaleaon_y_las_visitadoras-332228135-large.jpg")
                    .build());

            books.add(BookEntity.builder()
                    .title("El otoño del patriarca")
                    .description("Una novela sobre un dictador caribeño ficticio.")
                    .author(author1)
                    .isbn("9780060882860")
                    .publishedDate(LocalDate.of(1975, 6, 25))
                    .active(true)
                    .image("https://upload.wikimedia.org/wikipedia/commons/a/a8/El_oto%C3%B1o_del_patriarca.png")
                    .build());

            booksRepository.saveAll(books);
        }
    }

}
