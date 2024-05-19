package org.silver.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PaginationConfig {

    private PaginationConfig() {
    }

    // Pagination config
    public static Pageable setPagination(int page) {
        int size = 10;
        return PageRequest.of(page, size);
    }

}
