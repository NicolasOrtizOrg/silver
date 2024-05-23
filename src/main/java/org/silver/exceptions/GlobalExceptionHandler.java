package org.silver.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, Object> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        ResponseEntity<Object> response = ResponseEntity.status(status)
                .body(ErrorResponseDto.builder()
                        .message("Ha ocurrido un error")
                        .timestamp(LocalDateTime.now())
                        .error(errors)
                        .build());
        log.error(response);
        return response;
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(GenericException ex,
                                                                   HttpServletRequest request) {
        ResponseEntity<ErrorResponseDto> response = ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDto.builder()
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .uri(request.getRequestURI())
                        .build());
        log.error(response);
        return response;
    }

    @ExceptionHandler(ResourceDuplicate.class)
    public ResponseEntity<ErrorResponseDto> handleBookExistsException(ResourceDuplicate ex,
                                                                      HttpServletRequest request) {
        ResponseEntity<ErrorResponseDto> response = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder()
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .uri(request.getRequestURI())
                        .build());
        log.error(response);
        return response;
    }


}
