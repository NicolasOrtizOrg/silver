package org.silver.integration.google_books.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class GoogleApiModel {

    private String kind;
    private Long totalItems;
    private List<GoogleBookModel> items;
}
