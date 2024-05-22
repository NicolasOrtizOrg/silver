package org.silver.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder()
                        .message("ha ocurrido un error")
                        .timestamp(LocalDateTime.now())
                        .error(errors)
                        .build());
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(GenericException ex, HttpServletRequest request) {
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

    @ExceptionHandler(BookExistsEx.class)
    public ResponseEntity<ErrorResponseDto> handleBookExistsException(BookExistsEx ex, HttpServletRequest request) {
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

    @ExceptionHandler(BookNotFoundEx.class)
    public ResponseEntity<ErrorResponseDto> handleBookNotFoundException(BookNotFoundEx ex, HttpServletRequest request) {
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

    @ExceptionHandler(PlaylistNotFoundEx.class)
    public ResponseEntity<ErrorResponseDto> handlePlaylistNotFoundException(PlaylistNotFoundEx ex, HttpServletRequest request) {
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

    @ExceptionHandler(RelationNotFoundEx.class)
    public ResponseEntity<ErrorResponseDto> handleRelationNotFoundException(RelationNotFoundEx ex, HttpServletRequest request) {
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

}
