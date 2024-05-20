package org.silver.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter @ToString
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private LocalDate publishedDate;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne
    private AuthorEntity author;

}
