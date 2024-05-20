package org.silver.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@ToString
public class ErrorResponseDto {

    private String message;
    private String code;
    private LocalDateTime timestamp;
    private String uri;

}
