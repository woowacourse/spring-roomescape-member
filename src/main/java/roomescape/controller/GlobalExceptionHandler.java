package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_PREFIX = "[ERROR]";

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(formatErrorMessage(ex.getMessage()));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(formatErrorMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return new ResponseEntity<>(formatErrorMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder errorMessages = new StringBuilder();
        StringBuilder errorMessage;
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage = new StringBuilder();
            errorMessage.append(fieldError.getField())
                    .append(" 에러 발생: ")
                    .append(fieldError.getDefaultMessage())
                    .append("\n");
            errorMessages.append(formatErrorMessage(errorMessage.toString()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages.toString());
    }

    private String formatErrorMessage(String errorMessage) {
        return String.format("%s %s", ERROR_PREFIX, errorMessage);
    }
}
