package roomescape.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ReservationException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleBadRequestException(final RuntimeException ex, final WebRequest request) {
        return buildResponseEntity(ex, HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(final NotFoundException ex, final WebRequest request) {
        return buildResponseEntity(ex, HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(final UnauthorizedException ex,
                                                              final WebRequest request) {
        return buildResponseEntity(ex, HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenException(final ForbiddenException ex, final WebRequest request) {
        return buildResponseEntity(ex, HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }

    @ExceptionHandler(TokenCreationException.class)
    public ResponseEntity<Object> handleTokenCreationException(final TokenCreationException ex,
                                                               final WebRequest request) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR, "토큰 생성 중 오류가 발생했습니다.", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternalServerException(final Exception ex, final WebRequest request) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", request);
    }

    private ResponseEntity<Object> buildResponseEntity(
            final Exception ex,
            final HttpStatus status,
            final String message,
            final WebRequest request
    ) {
        final String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        final ProblemDetail body = super.createProblemDetail(ex, status, message, path, null, request);
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }
}
