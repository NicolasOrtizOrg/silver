package org.silver.integration.google_books.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VolumeInfo {

    private String title;
    private String description;
    private String publisher;

    private String publishedDate;
    private List<String> authors;
    private ImageLinks imageLinks;
    private List<IndustryIdentifiers> industryIdentifiers;

}
