package roomescape.common.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import roomescape.common.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ApiExceptionHandlerSupport {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String INVALID_REQUEST_VALUE_MESSAGE = "요청 값이 올바르지 않습니다.";
    private static final String INVALID_REQUEST_BODY_MESSAGE = "요청 본문 형식이 올바르지 않습니다.";
    private static final String DEFAULT_FORMAT_MESSAGE = "요청 값 형식이 올바르지 않습니다.";
    private static final String DATE_FORMAT_MESSAGE = "날짜는 yyyy-MM-dd 형식이어야 합니다.";
    private static final String TIME_FORMAT_MESSAGE = "예약 시간은 HH:mm 형식이어야 합니다.";
    private static final String DURATION_TIME_FORMAT_MESSAGE = "진행 시간은 HH:mm:ss 형식이어야 합니다.";
    private static final Map<String, String> FIELD_FORMAT_MESSAGES = Map.of(
            "date", DATE_FORMAT_MESSAGE,
            "startAt", TIME_FORMAT_MESSAGE,
            "time.startAt", TIME_FORMAT_MESSAGE,
            "durationTime", DURATION_TIME_FORMAT_MESSAGE,
            "themeId", "테마 ID는 숫자여야 합니다.",
            "timeId", "예약 시간 ID는 숫자여야 합니다.",
            "id", "ID는 숫자여야 합니다."
    );

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(this::toValidationMessage)
                .collect(Collectors.joining(", "));
        if (message.isBlank()) {
            message = INVALID_REQUEST_VALUE_MESSAGE;
        }
        log.warn(
                "Validation error [{} {}]: {}",
                request.getMethod(),
                request.getRequestURI(),
                message,
                e
        );
        return badRequest(message);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(this::toValidationMessage)
                .collect(Collectors.joining(", "));
        if (message.isBlank()) {
            message = INVALID_REQUEST_VALUE_MESSAGE;
        }
        log.warn(
                "Binding error [{} {}]: {}",
                request.getMethod(),
                request.getRequestURI(),
                message,
                e
        );
        return badRequest(message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e, HttpServletRequest request) {
        String message = INVALID_REQUEST_BODY_MESSAGE;
        if (e.getCause() instanceof InvalidFormatException invalidFormatException) {
            message = toFormatMessage(extractFieldName(invalidFormatException), invalidFormatException.getTargetType());
        }
        log.warn(
                "Request body parse error [{} {}]: {}",
                request.getMethod(),
                request.getRequestURI(),
                message,
                e
        );
        return badRequest(message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String message = toFormatMessage(e.getName(), e.getRequiredType());
        log.warn(
                "Type mismatch [{} {}]: {}",
                request.getMethod(),
                request.getRequestURI(),
                message,
                e
        );
        return badRequest(message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException e, HttpServletRequest request) {
        log.warn(
                "Illegal argument [{} {}]: {}",
                request.getMethod(),
                request.getRequestURI(),
                e.getMessage(),
                e
        );
        return badRequest(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException e, HttpServletRequest request) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        if (message.isBlank()) {
            message = INVALID_REQUEST_VALUE_MESSAGE;
        }
        log.warn(
                "Constraint violation [{} {}]: {}",
                request.getMethod(),
                request.getRequestURI(),
                message,
                e
        );
        return badRequest(message);
    }

    private String toValidationMessage(FieldError error) {
        if (error.isBindingFailure()) {
            return toFormatMessage(error.getField(), null);
        }
        return error.getDefaultMessage();
    }

    private String extractFieldName(InvalidFormatException e) {
        return e.getPath().stream()
                .map(reference -> reference.getFieldName())
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(INVALID_REQUEST_VALUE_MESSAGE);
    }

    private String toFormatMessage(String fieldName, Class<?> targetType) {
        if (targetType != null && LocalDate.class.isAssignableFrom(targetType)) {
            return DATE_FORMAT_MESSAGE;
        }
        if (targetType != null && LocalTime.class.isAssignableFrom(targetType)) {
            if ("durationTime".equals(fieldName)) {
                return DURATION_TIME_FORMAT_MESSAGE;
            }
            return TIME_FORMAT_MESSAGE;
        }
        return FIELD_FORMAT_MESSAGES.getOrDefault(fieldName, DEFAULT_FORMAT_MESSAGE);
    }
}
