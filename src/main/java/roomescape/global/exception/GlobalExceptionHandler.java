package roomescape.global.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.auth.exception.ForbiddenException;
import roomescape.auth.exception.UnauthorizedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e) {
        ErrorResponse error = ErrorResponse.create(e, HttpStatus.NOT_FOUND, e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ErrorResponse> handleSpringDaoException(RuntimeException e) {
        ErrorResponse error = ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class, EntityDuplicateException.class})
    public ResponseEntity<ErrorResponse> handleServiceLogicException(IllegalArgumentException e) {
        ErrorResponse error = ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        ErrorResponse error = ErrorResponse.create(e, HttpStatus.UNAUTHORIZED, e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException e) {
        ErrorResponse error = ErrorResponse.create(e, HttpStatus.FORBIDDEN, e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
