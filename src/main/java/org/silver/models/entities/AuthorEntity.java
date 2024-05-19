package org.silver.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@ToString
@NoArgsConstructor @AllArgsConstructor
@Table(name = "authors")
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

}
