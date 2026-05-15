package roomescape.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(DuplicateTimeException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateTimeException(DuplicateTimeException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "파라미터 값이 유효하지 않습니다.");
        problemDetail.setProperty("code", "INVALID_PARAMETER");
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

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다.");
        problemDetail.setProperty("code", "MISSING_PARAMETER");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "요청 값이 유효하지 않습니다.");
        problemDetail.setProperty("code", "INVALID_REQUEST_BODY");
        problemDetail.setProperty("errors", extractErrors(ex));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    private List<Map<String, String>> extractErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(this::toErrorMap)
                .toList();
    }

    private Map<String, String> toErrorMap(FieldError error) {
        return Map.of(
                "field", error.getField(),
                "message", error.getDefaultMessage()
        );
    }
}
