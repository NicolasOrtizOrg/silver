package org.silver.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

//@DynamicUpdate
@Entity
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 255, message = "El título deber tener entre 2 y 255 caracteres")
    @Column(nullable = false)
    private String title;

    @Column(length = 10000)
    private String description;

    @Column(length = 1000)
    private String image;

    @Column(nullable = false, unique = true)
    private String isbn;

    private LocalDate publishedDate;

    private boolean active;

    @ManyToOne
    private AuthorEntity author;

}
