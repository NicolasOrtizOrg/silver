package org.silver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RelationNotFoundEx extends RuntimeException{
    public RelationNotFoundEx(String message) {
        super(message);
    }
}
