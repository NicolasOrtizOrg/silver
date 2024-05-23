package org.silver.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class ErrorResponseDto {

    private String message;
    private LocalDateTime timestamp;
    private String uri;
    private Object error;

}
