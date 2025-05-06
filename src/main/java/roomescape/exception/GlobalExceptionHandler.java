package roomescape.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.auth.ForbiddenException;
import roomescape.exception.auth.LoginFailException;
import roomescape.exception.auth.NotAuthenticatedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotAuthenticatedException.class)
    public ResponseEntity<ErrorResponse> handle(NotAuthenticatedException e) {
        logger.warn("Handled ForbiddenException: {}", e.getMessage(), e);
        return createResponse(HttpStatus.UNAUTHORIZED, e);
    }

    @ExceptionHandler(LoginFailException.class)
    public ResponseEntity<ErrorResponse> handle(LoginFailException e) {
        logger.warn("Handled ForbiddenException: {}", e.getMessage(), e);
        return createResponse(HttpStatus.UNAUTHORIZED, e);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handle(ForbiddenException e) {
        logger.warn("Handled ForbiddenException: {}", e.getMessage(), e);
        return createResponse(HttpStatus.FORBIDDEN, e);
    }

    @ExceptionHandler(RootBusinessException.class)
    public ResponseEntity<ErrorResponse> handle(RootBusinessException e) {
        logger.warn("Handled RootException: {}", e.getMessage(), e);
        return createResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e) {
        logger.error("Handled Exception: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(ErrorResponse.withoutDetailMessage());
    }

    private static ResponseEntity<ErrorResponse> createResponse(HttpStatus status, Exception e) {
        return ResponseEntity.status(status).body(ErrorResponse.withDetailMessage(status, e));
    }
}
