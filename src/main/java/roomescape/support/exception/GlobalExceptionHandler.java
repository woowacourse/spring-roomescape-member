package roomescape.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return ErrorResponse.of(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException e) {
        return ErrorResponse.of(HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(InternalServerException e) {
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        String message = e.getAllErrors()
            .stream()
            .findFirst()
            .map(ObjectError::getDefaultMessage)
            .orElse(RoomescapeErrors.INPUT_VALIDATION_ERROR.getMessage());

        return ErrorResponse.of(HttpStatus.BAD_REQUEST, RoomescapeErrors.INPUT_VALIDATION_ERROR, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e
    ) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, RoomescapeErrors.INPUT_FORMAT_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, RoomescapeErrors.INTERNAL_SERVER_ERROR);
    }
}
