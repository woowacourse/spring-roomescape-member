package roomescape.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, exception.getErrorCode());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        return ErrorResponse.of(HttpStatus.NOT_FOUND, exception.getErrorCode());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException exception) {
        return ErrorResponse.of(HttpStatus.CONFLICT, exception.getErrorCode());
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(InternalServerException exception) {
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, RoomescapeErrorCode.INTERNAL_SERVER_ERROR);
    }
}
