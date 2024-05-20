package org.silver.integration.google_books;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleBookModel {

    private String id;
    private VolumeInfo volumeInfo;

}

@Getter
@Setter
class VolumeInfo {

    private String title;
    private String description;
    private String publisher;

    private String publishedDate;
    private List<String> authors;
    private ImageLinks imageLinks;
    private List<IndustryIdentifiers> industryIdentifiers;

}

@Getter
@Setter
class ImageLinks {
    private String smallThumbnail;
    private String thumbnail;
    private String small;
    private String medium;
    private String large;
}

@Getter
@Setter
class IndustryIdentifiers {
    private String type;
    private String identifier;
}
