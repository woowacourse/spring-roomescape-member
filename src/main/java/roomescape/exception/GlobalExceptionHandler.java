package roomescape.exception;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import roomescape.exception.business.BusinessException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("유효하지 않은 입력값: {}", message);
        return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_INPUT", message));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("읽을 수 없는 요청: {}", e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_INPUT", "요청 본문을 읽을 수 없습니다."));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception e, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("Spring MVC 예외: {}", e.getMessage());
        return ResponseEntity.status(status).body(new ErrorResponse(status.toString(), e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("잘못된 인자: {}", e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_INPUT", e.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException e) {
        log.warn("비즈니스 규칙 위반 [{}]: {}", e.getStatus(), e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(e.getStatus().name(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
        log.error("예상치 못한 오류 발생", e);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("INTERNAL_SERVER_ERROR", "일시적인 오류가 발생했습니다."));
    }
}