package roomescape.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String TYPE_BASE = "https://roomescape.example/problems/";

    private static final String TYPE_NOT_FOUND = "not-found";
    private static final String TYPE_CONFLICT = "conflict";
    private static final String TYPE_BUSINESS_RULE_VIOLATION = "business-rule-violation";
    private static final String TYPE_VALIDATION_ERROR = "validation-error";
    private static final String TYPE_BAD_REQUEST = "bad-request";
    private static final String TYPE_METHOD_NOT_SUPPORTED = "method-not-supported";
    private static final String TYPE_MEDIA_TYPE_NOT_SUPPORTED = "media-type-not-supported";
    private static final String TYPE_NOT_ACCEPTABLE = "not-acceptable";
    private static final String TYPE_NO_RESOURCE = "no-resource";
    private static final String TYPE_INTERNAL_ERROR = "internal-error";

    private static final String TITLE_NOT_FOUND = "리소스를 찾을 수 없음";
    private static final String TITLE_CONFLICT = "요청이 현재 상태와 충돌함";
    private static final String TITLE_BUSINESS_RULE_VIOLATION = "비즈니스 정책 위반";
    private static final String TITLE_VALIDATION_ERROR = "요청 본문 검증 실패";
    private static final String TITLE_BAD_REQUEST = "잘못된 요청";
    private static final String TITLE_METHOD_NOT_SUPPORTED = "지원하지 않는 HTTP 메서드";
    private static final String TITLE_MEDIA_TYPE_NOT_SUPPORTED = "지원하지 않는 미디어 타입";
    private static final String TITLE_NOT_ACCEPTABLE = "응답 가능한 미디어 타입 없음";
    private static final String TITLE_NO_RESOURCE = "리소스를 찾을 수 없음";
    private static final String TITLE_INTERNAL_ERROR = "서버 내부 오류";

    private static final String DETAIL_VALIDATION_ERROR = "요청 본문의 일부 필드가 유효하지 않습니다.";
    private static final String DETAIL_INTERNAL_ERROR = "요청을 처리하는 중 알 수 없는 오류가 발생했습니다.";

    private static final String ERRORS_PROPERTY = "errors";
    private static final String POINTER_PREFIX = "/";

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex, WebRequest request) {
        return build(HttpStatus.NOT_FOUND, TYPE_NOT_FOUND, TITLE_NOT_FOUND, ex.getMessage(), ex, request);
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflict(ConflictException ex, WebRequest request) {
        return build(HttpStatus.CONFLICT, TYPE_CONFLICT, TITLE_CONFLICT, ex.getMessage(), ex, request);
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ProblemDetail handleBusinessRuleViolation(BusinessRuleViolationException ex, WebRequest request) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, TYPE_BUSINESS_RULE_VIOLATION, TITLE_BUSINESS_RULE_VIOLATION, ex.getMessage(), ex, request);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex, WebRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, TYPE_INTERNAL_ERROR, TITLE_INTERNAL_ERROR, DETAIL_INTERNAL_ERROR, ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, DETAIL_VALIDATION_ERROR);
        List<FieldErrorDetail> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldErrorDetail::from)
                .toList();
        problem.setProperty(ERRORS_PROPERTY, errors);
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        ResponseEntity<Object> response = super.handleExceptionInternal(ex, body, headers, status, request);
        if (response != null && response.getBody() instanceof ProblemDetail problem) {
            decorate(problem, ex, status, request);
        }
        logException(ex, status, request);
        return response;
    }

    private ProblemDetail build(HttpStatus status, String typeSlug, String title, String detail, Exception ex, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setType(URI.create(TYPE_BASE + typeSlug));
        problem.setTitle(title);
        problem.setInstance(URI.create(extractUri(request)));
        logException(ex, status, request);
        return problem;
    }

    private void decorate(ProblemDetail problem, Exception ex, HttpStatusCode status, WebRequest request) {
        SlugAndTitle mapping = mappingFor(ex, status);
        problem.setType(URI.create(TYPE_BASE + mapping.slug()));
        problem.setTitle(mapping.title());
        problem.setInstance(URI.create(extractUri(request)));
    }

    private SlugAndTitle mappingFor(Exception ex, HttpStatusCode status) {
        if (ex instanceof MethodArgumentNotValidException) {
            return new SlugAndTitle(TYPE_VALIDATION_ERROR, TITLE_VALIDATION_ERROR);
        }
        return switch (status.value()) {
            case 400 -> new SlugAndTitle(TYPE_BAD_REQUEST, TITLE_BAD_REQUEST);
            case 404 -> new SlugAndTitle(TYPE_NO_RESOURCE, TITLE_NO_RESOURCE);
            case 405 -> new SlugAndTitle(TYPE_METHOD_NOT_SUPPORTED, TITLE_METHOD_NOT_SUPPORTED);
            case 406 -> new SlugAndTitle(TYPE_NOT_ACCEPTABLE, TITLE_NOT_ACCEPTABLE);
            case 415 -> new SlugAndTitle(TYPE_MEDIA_TYPE_NOT_SUPPORTED, TITLE_MEDIA_TYPE_NOT_SUPPORTED);
            default -> new SlugAndTitle(TYPE_INTERNAL_ERROR, TITLE_INTERNAL_ERROR);
        };
    }

    private void logException(Exception ex, HttpStatusCode status, WebRequest request) {
        String method = extractMethod(request);
        String uri = extractUri(request);
        int code = status.value();
        if (status.is5xxServerError()) {
            log.error("{} {} → {}", method, uri, code, ex);
            return;
        }
        log.warn("{} {} → {} {}", method, uri, code, ex.getMessage());
    }

    private String extractUri(WebRequest request) {
        if (request instanceof ServletWebRequest servletRequest) {
            return servletRequest.getRequest().getRequestURI();
        }
        return "";
    }

    private String extractMethod(WebRequest request) {
        if (request instanceof ServletWebRequest servletRequest) {
            return servletRequest.getRequest().getMethod();
        }
        return "";
    }

    private record SlugAndTitle(String slug, String title) {
    }

    public record FieldErrorDetail(String pointer, String reason) {
        public static FieldErrorDetail from(FieldError fieldError) {
            return new FieldErrorDetail(POINTER_PREFIX + fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}
