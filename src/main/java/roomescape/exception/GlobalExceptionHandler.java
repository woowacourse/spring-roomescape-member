package roomescape.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorStatusMapper errorStatusMapper;

    public GlobalExceptionHandler(ErrorStatusMapper errorStatusMapper) {
        this.errorStatusMapper = errorStatusMapper;
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException exception, HttpServletRequest request) {
        log.error("Domain exception occurred", exception);
        ErrorCode errorCode = exception.getErrorCode();

        return ResponseEntity
                .status(errorStatusMapper.map(errorCode))
                .body(ErrorResponse.of(request.getRequestURI(), errorCode.message()));
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ErrorResponse> handleInfrastructureException(InfrastructureException exception, HttpServletRequest request) {
        log.error("Infrastructure exception occurred", exception);
        return internalServerError(request.getRequestURI());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception,
            Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request
    ) {
        log.error("Spring Mvc Internal exception occurred", exception);
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        return ResponseEntity
                .status(statusCode)
                .headers(headers)
                .body(ErrorResponse.of(path, exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception exception, HttpServletRequest request) {
        log.error("Unexpected exception occurred", exception);
        return internalServerError(request.getRequestURI());
    }


    private ResponseEntity<ErrorResponse> internalServerError(String path) {
        return ResponseEntity
                .internalServerError()
                .body(ErrorResponse.of(path, "서버 내부에서 문제가 발생했습니다."));
    }
}
