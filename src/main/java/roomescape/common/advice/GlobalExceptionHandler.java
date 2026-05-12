package roomescape.common.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(this::toValidationMessage)
                .collect(Collectors.joining(", "));
        if (message.isBlank()) {
            message = "요청 값이 올바르지 않습니다.";
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
            message = "요청 값이 올바르지 않습니다.";
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
        String message = "요청 본문 형식이 올바르지 않습니다.";
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandledException(Exception e, HttpServletRequest request) {
        log.error(
                "Unhandled exception [{} {}]",
                request.getMethod(),
                request.getRequestURI(),
                e
        );
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "요청 처리 중 문제가 발생했습니다. 잠시 후 다시 시도해 주세요.");
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
                .orElse("요청 값이 올바르지 않습니다.");
    }

    private String toFormatMessage(String fieldName, Class<?> targetType) {
        if (targetType != null && LocalDate.class.isAssignableFrom(targetType)) {
            return "날짜는 yyyy-MM-dd 형식이어야 합니다.";
        }
        if (targetType != null && LocalTime.class.isAssignableFrom(targetType)) {
            if ("durationTime".equals(fieldName)) {
                return "진행 시간은 HH:mm:ss 형식이어야 합니다.";
            }
            return "예약 시간은 HH:mm 형식이어야 합니다.";
        }
        return switch (fieldName) {
            case "date" -> "날짜는 yyyy-MM-dd 형식이어야 합니다.";
            case "startAt", "time.startAt" -> "예약 시간은 HH:mm 형식이어야 합니다.";
            case "durationTime" -> "진행 시간은 HH:mm:ss 형식이어야 합니다.";
            case "themeId" -> "테마 ID는 숫자여야 합니다.";
            case "timeId" -> "예약 시간 ID는 숫자여야 합니다.";
            case "id" -> "ID는 숫자여야 합니다.";
            default -> "요청 값 형식이 올바르지 않습니다.";
        };
    }
}
