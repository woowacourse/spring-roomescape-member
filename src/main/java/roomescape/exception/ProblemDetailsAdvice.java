package roomescape.exception;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ProblemDetailsAdvice extends ResponseEntityExceptionHandler {

    private final Map<Class<? extends RoomescapeException>, HttpStatus> exceptionHttpStatusMap = new ConcurrentHashMap<>();

    public ProblemDetailsAdvice() {
        exceptionHttpStatusMap.put(ReservationNotFoundException.class, HttpStatus.NOT_FOUND);
        exceptionHttpStatusMap.put(ThemeNotFoundException.class, HttpStatus.NOT_FOUND);
        exceptionHttpStatusMap.put(TimeSlotNotFoundException.class, HttpStatus.NOT_FOUND);
        exceptionHttpStatusMap.put(InvalidOwnershipException.class, HttpStatus.FORBIDDEN);
        exceptionHttpStatusMap.put(PastReservationControlException.class, HttpStatus.BAD_REQUEST);
        exceptionHttpStatusMap.put(PastTimeException.class, HttpStatus.BAD_REQUEST);
        exceptionHttpStatusMap.put(ResourceInUseException.class, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RoomescapeException.class)
    public ResponseEntity<ProblemDetail> handleDomainException(RoomescapeException exception) {
        HttpStatus status = exceptionHttpStatusMap.getOrDefault(exception.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
        problemDetail.setProperty("code", exception.getErrorCode());

        return ResponseEntity.status(status).body(problemDetail);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateKeyException(DuplicateKeyException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String detailMessage = "해당 경로에서는 " + ex.getMethod() + " 요청을 지원하지 않습니다. API 명세를 확인해 주세요.";

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, detailMessage);
        problemDetail.setProperty("code", "METHOD_NOT_ALLOWED");

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(problemDetail);
    }
}
