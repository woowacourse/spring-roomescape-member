package roomescape.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.auth.AuthenticationException;
import roomescape.exception.auth.AuthorizationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RootBusinessException.class)
    public ResponseEntity<ErrorResponse> handle(RootBusinessException e) {
        logger.warn("Handled RootException: {}", e.getMessage(), e);
        return ErrorResponse.plainResponse(HttpStatus.BAD_REQUEST, e.code()).toResponseEntity();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handle(AuthenticationException e) {
        logger.warn("Handled AuthenticatedException: {}", e.detailMessage(), e);
        return ErrorResponse.securedResponse(HttpStatus.UNAUTHORIZED, e.clientMessage()).toResponseEntity();
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handle(AuthorizationException e) {
        logger.warn("Handled AuthorizationException: {}", e.detailMessage(), e);
        return ErrorResponse.securedResponse(HttpStatus.FORBIDDEN, e.clientMessage()).toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e) {
        logger.error("Handled Exception: {}", e.getMessage(), e);
        return ErrorResponse.securedResponse().toResponseEntity();
    }
}
