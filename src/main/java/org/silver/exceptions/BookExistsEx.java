package org.silver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookExistsEx extends RuntimeException{
    public BookExistsEx(String message) {
        super(message);
    }
}
