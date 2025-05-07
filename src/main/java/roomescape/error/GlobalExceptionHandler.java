package roomescape.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ReservationException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleBadRequestException(final Exception ex, final WebRequest request) {
        final ProblemDetail body = super.createProblemDetail(ex, HttpStatus.BAD_REQUEST, ex.getMessage(), null,
                null, request);
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(final Exception ex, final WebRequest request) {
        final ProblemDetail body = super.createProblemDetail(ex, HttpStatus.NOT_FOUND, ex.getMessage(), null,
                null, request);
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
