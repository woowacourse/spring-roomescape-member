package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidNameException(final BadRequestException e) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException e) {
        return createErrorResponse(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedException(final ConflictException e) {
        return createErrorResponse(HttpStatus.CONFLICT, e);
    }

    public ResponseEntity<ErrorResponse> createErrorResponse(final HttpStatus httpStatus, final Exception e) {
        return ResponseEntity.status(httpStatus)
                .body(new ErrorResponse(e.getMessage()));
    }
}
