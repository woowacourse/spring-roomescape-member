package roomescape.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.auth.ForbiddenException;
import roomescape.exception.auth.LoginExpiredException;
import roomescape.exception.auth.LoginFailException;
import roomescape.exception.auth.NotAuthenticatedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotAuthenticatedException.class)
    public ResponseEntity<ErrorResponse> handle(NotAuthenticatedException e) {
        logger.warn("Handled NotAuthenticatedException: {}", e.getMessage(), e);
        return ErrorResponse.withDetailMessage(HttpStatus.UNAUTHORIZED, e, "로그인이 필요합니다.").toResponseEntity();
    }

    @ExceptionHandler(LoginFailException.class)
    public ResponseEntity<ErrorResponse> handle(LoginFailException e) {
        logger.warn("Handled LoginFailException: {}", e.getMessage(), e);
        return ErrorResponse.withDetailMessage(HttpStatus.UNAUTHORIZED, e, "로그인에 실패했습니다.").toResponseEntity();
    }

    @ExceptionHandler(LoginExpiredException.class)
    public ResponseEntity<ErrorResponse> handle(LoginExpiredException e) {
        logger.warn("Handled LoginExpiredException: {}", e.getMessage(), e);
        return ErrorResponse.withDetailMessage(HttpStatus.FORBIDDEN, e, "다시 로그인해주세요.").toResponseEntity();
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handle(ForbiddenException e) {
        logger.warn("Handled ForbiddenException: {}", e.getMessage(), e);
        return ErrorResponse.withDetailMessage(HttpStatus.FORBIDDEN, e, "권한이 없습니다.").toResponseEntity();
    }

    @ExceptionHandler(RootBusinessException.class)
    public ResponseEntity<ErrorResponse> handle(RootBusinessException e) {
        logger.warn("Handled RootException: {}", e.getMessage(), e);
        return ErrorResponse.withDetailMessage(HttpStatus.BAD_REQUEST, e.code()).toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e) {
        logger.error("Handled Exception: {}", e.getMessage(), e);
        return ErrorResponse.withoutDetailMessage().toResponseEntity();
    }
}
