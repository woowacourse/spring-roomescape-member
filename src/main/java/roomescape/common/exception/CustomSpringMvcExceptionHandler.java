package roomescape.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Objects;

import static roomescape.common.exception.GlobalErrorCode.VALIDATION_ERROR;

/**
 * Spring Mvc에서 던지는 4xx번대 예외들을 처리
 */
@RestControllerAdvice
@Slf4j
@Order(0)
public class CustomSpringMvcExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * MethodArgumentNotValidException: Bean Validation 오류
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request
    ) {
        log.error("Method Argument exception occurred", exception);
        List<String> messages = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .filter(Objects::nonNull)
                .toList();
        if (messages.isEmpty()) {
            messages = List.of("잘못된 요청입니다.");
        }

        return ResponseEntity
                .status(statusCode)
                .headers(headers)
                .body(ErrorResponse.of(pathFrom(request), VALIDATION_ERROR, messages));
    }

    /**
     * HttpMessageNotReadableException:
     * 요청 body JSON 자체를 못 읽을 때. 예시: date: "2024/01/01"처럼 LocalDate 파싱 실패
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return validationError(headers, request, "요청 본문 형식이 올바르지 않습니다.");
    }

    /**
     * MissingServletRequestParameterException: @RequestParam이 없을 때
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return validationError(headers, request, ex.getParameterName() + " 파라미터는 필수입니다.");
    }

    /**
     * MethodArgumentTypeMismatchException: @PathVariable, @RequestParam 타입 변환 실패
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return validationError(headers, request, ex.getPropertyName() + " 값의 타입이 올바르지 않습니다.");
    }

    private ResponseEntity<Object> validationError(
            HttpHeaders headers, WebRequest request, String message
    ) {
        return ResponseEntity
                .status(VALIDATION_ERROR.status())
                .headers(headers)
                .body(ErrorResponse.of(pathFrom(request), VALIDATION_ERROR.code(), message));
    }

    private String pathFrom(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }
}
