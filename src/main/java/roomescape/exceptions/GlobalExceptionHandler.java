package roomescape.exceptions;

import java.time.LocalDateTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException e) {
        return createErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return createErrorResponse(e, HttpStatus.BAD_REQUEST, "데이터 무결성 위반이 발생했습니다.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException e) {
        return createErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityDuplicateException.class)
    public ProblemDetail handleEntityDuplicate(EntityDuplicateException e) {
        return createErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e) {
        return createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");
    }

    private ProblemDetail createErrorResponse(Exception e, HttpStatus status) {
        return createErrorResponse(e, status, e.getMessage());
    }

    private ProblemDetail createErrorResponse(Exception e, HttpStatus status, String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
        problemDetail.setTitle(e.getClass().getSimpleName());
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        e.printStackTrace();
        return problemDetail;
    }
}
