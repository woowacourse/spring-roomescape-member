package roomescape.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ReservationException.class, MemberException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleBadRequestException(final Exception ex, final WebRequest request) {
        log.info("BadRequestException: {}", ex.getMessage());
        final ProblemDetail body = super.createProblemDetail(ex, HttpStatus.BAD_REQUEST, ex.getMessage(), null,
                null, request);
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(final Exception ex, final WebRequest request) {
        log.info("NotFoundException: {}", ex.getMessage());
        final ProblemDetail body = super.createProblemDetail(ex, HttpStatus.NOT_FOUND, ex.getMessage(), null,
                null, request);
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthException(final Exception ex, final WebRequest request) {
        log.info("AuthException: {}", ex.getMessage());
        final ProblemDetail body = super.createProblemDetail(ex, HttpStatus.UNAUTHORIZED, ex.getMessage(), null,
                null, request);
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(final Exception ex, final WebRequest request) {
        log.info("AccessDeniedException: {}", ex.getMessage());
        final ProblemDetail body = super.createProblemDetail(ex, HttpStatus.FORBIDDEN, ex.getMessage(), null,
                null, request);
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternalServerError(final Exception ex, final WebRequest request) {
        log.error("InternalServerError: {}", ex.getMessage(), ex);
        final ProblemDetail body = super.createProblemDetail(ex, HttpStatus.INTERNAL_SERVER_ERROR,
                "서버에서 알 수 없는 오류가 발생했습니다.", null, null, request);
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
