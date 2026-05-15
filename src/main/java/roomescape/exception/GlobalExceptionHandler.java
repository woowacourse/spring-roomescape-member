package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String TYPE_BASE = "https://roomescape.example/problems/";

    private static final String TYPE_NOT_FOUND = "not-found";
    private static final String TYPE_CONFLICT = "conflict";
    private static final String TYPE_BUSINESS_RULE_VIOLATION = "business-rule-violation";
    private static final String TYPE_VALIDATION_ERROR = "validation-error";
    private static final String TYPE_MALFORMED_REQUEST_BODY = "malformed-request-body";
    private static final String TYPE_INTERNAL_ERROR = "internal-error";

    private static final String TITLE_NOT_FOUND = "리소스를 찾을 수 없음";
    private static final String TITLE_CONFLICT = "요청이 현재 상태와 충돌함";
    private static final String TITLE_BUSINESS_RULE_VIOLATION = "비즈니스 정책 위반";
    private static final String TITLE_VALIDATION_ERROR = "요청 본문 검증 실패";
    private static final String TITLE_MALFORMED_REQUEST_BODY = "요청 본문을 해석할 수 없음";
    private static final String TITLE_INTERNAL_ERROR = "서버 내부 오류";

    private static final String DETAIL_VALIDATION_ERROR = "요청 본문의 일부 필드가 유효하지 않습니다.";
    private static final String DETAIL_MALFORMED_REQUEST_BODY = "요청 본문의 형식이 올바르지 않습니다.";
    private static final String DETAIL_INTERNAL_ERROR = "요청을 처리하는 중 알 수 없는 오류가 발생했습니다.";

    private static final String ERRORS_PROPERTY = "errors";
    private static final String POINTER_PREFIX = "/";

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException e) {
        return problemDetail(HttpStatus.NOT_FOUND, TITLE_NOT_FOUND, TYPE_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflict(ConflictException e) {
        return problemDetail(HttpStatus.CONFLICT, TITLE_CONFLICT, TYPE_CONFLICT, e.getMessage());
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ProblemDetail handleBusinessRuleViolation(BusinessRuleViolationException e) {
        return problemDetail(HttpStatus.UNPROCESSABLE_ENTITY, TITLE_BUSINESS_RULE_VIOLATION, TYPE_BUSINESS_RULE_VIOLATION, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException e) {
        ProblemDetail problem = problemDetail(
                HttpStatus.BAD_REQUEST,
                TITLE_VALIDATION_ERROR,
                TYPE_VALIDATION_ERROR,
                DETAIL_VALIDATION_ERROR
        );
        List<FieldErrorDetail> errors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldErrorDetail::from)
                .toList();
        problem.setProperty(ERRORS_PROPERTY, errors);
        return problem;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadable(HttpMessageNotReadableException e) {
        return problemDetail(
                HttpStatus.BAD_REQUEST,
                TITLE_MALFORMED_REQUEST_BODY,
                TYPE_MALFORMED_REQUEST_BODY,
                DETAIL_MALFORMED_REQUEST_BODY
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception e) {
        return problemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                TITLE_INTERNAL_ERROR,
                TYPE_INTERNAL_ERROR,
                DETAIL_INTERNAL_ERROR
        );
    }

    private ProblemDetail problemDetail(HttpStatus status, String title, String typeSlug, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setType(URI.create(TYPE_BASE + typeSlug));
        return problem;
    }

    public record FieldErrorDetail(String pointer, String reason) {
        public static FieldErrorDetail from(FieldError fieldError) {
            return new FieldErrorDetail(POINTER_PREFIX + fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}
