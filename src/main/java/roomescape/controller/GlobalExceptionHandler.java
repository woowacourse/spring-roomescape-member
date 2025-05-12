package roomescape.controller;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exceptions.EntityDuplicateException;
import roomescape.exceptions.EntityNotFoundException;
import roomescape.exceptions.auth.AuthenticationException;
import roomescape.exceptions.auth.AuthorizationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthorizationException.class)
    public ProblemDetail handleAuthorizationException(AuthorizationException e) {
        logger.warn(e.getMessage());
        return createErrorResponse(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException e) {
        logger.warn(e.getMessage());
        return createErrorResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleJsonParseException(HttpMessageNotReadableException e) {
        logger.warn(e.getMessage());
        return createErrorResponse(e, HttpStatus.BAD_REQUEST, "요청된 JSON의 형태가 잘못되었습니다.");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException e) {
        logger.warn(e.getMessage());
        return createErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityDuplicateException.class)
    public ProblemDetail handleEntityDuplicate(EntityDuplicateException e) {
        logger.warn(e.getMessage());
        return createErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException e) {
        logger.warn(e.getMessage());
        return createErrorResponse(e, HttpStatus.BAD_REQUEST, "데이터 무결성 위반이 발생했습니다.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException e) {
        logger.warn(e.getMessage());
        return createErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e) {
        logger.error(e.getMessage());
        return createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");
    }

    private ProblemDetail createErrorResponse(Exception e, HttpStatus status) {
        return createErrorResponse(e, status, e.getMessage());
    }

    private ProblemDetail createErrorResponse(Exception e, HttpStatus status, String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
        problemDetail.setTitle(e.getClass().getSimpleName());
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        logger.debug("Exception stack trace:", e);
        return problemDetail;
    }
}
