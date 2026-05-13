package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.dto.ErrorResponse;
import roomescape.exception.DuplicatedResourceException;
import roomescape.exception.ResourceDeleteConflicted;
import roomescape.exception.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "ILLEGAL_ARGUMENT",
                e.getMessage()
        ));
    }

    @ExceptionHandler(DuplicatedResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handlerDuplicatedException(DuplicatedResourceException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                e.getCode(),
                e.getMessage()
        ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handlerNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getCode(),
                e.getMessage()
        ));
    }

    @ExceptionHandler(ResourceDeleteConflicted.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handlerConflictedException(ResourceDeleteConflicted e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                e.getCode(),
                e.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handlerException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "SERVER_ERROR",
                "서버 내부에 문제가 발생했습니다"));

    }
}
