package org.silver.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ErrorResponse {

    private String message;
    private String code;

}
