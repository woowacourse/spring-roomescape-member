package roomescape.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(final ApiException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(new ErrorResponse(
                        exception.getCode(),
                        exception.getStatus().value(),
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException exception
    ) {
        FieldError fieldError = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .orElse(null);
        String message = Objects.isNull(fieldError) ? "유효하지 않은 입력입니다." : fieldError.getDefaultMessage();
        String code = Objects.isNull(fieldError) ? "INVALID_INPUT" : resolveValidationCode(fieldError);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(code, HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(final BindException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .orElse(null);
        String message = Objects.isNull(fieldError) ? "유효하지 않은 입력입니다." : fieldError.getDefaultMessage();
        String code = Objects.isNull(fieldError)
                ? "INVALID_INPUT"
                : resolveBindingCode(fieldError);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(code, HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            final HttpMessageNotReadableException exception
    ) {
        return buildHttpMessageNotReadableResponse(exception);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            final MethodArgumentTypeMismatchException exception
    ) {
        String code = resolveTypeMismatchCode(exception.getRequiredType());
        String message = resolveTypeMismatchMessage(exception.getRequiredType());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(code, HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "INTERNAL_SERVER_ERROR",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "서버 내부 오류가 발생했습니다."
                ));
    }

    private ResponseEntity<ErrorResponse> buildHttpMessageNotReadableResponse(
            final HttpMessageNotReadableException exception
    ) {
        Throwable cause = exception.getMostSpecificCause();

        if (cause instanceof InvalidFormatException invalidFormatException) {
            Class<?> targetType = invalidFormatException.getTargetType();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(
                            resolveTypeMismatchCode(targetType),
                            HttpStatus.BAD_REQUEST.value(),
                            resolveTypeMismatchMessage(targetType)
                    ));
        }

        if (cause instanceof DateTimeParseException) {
            String message = exception.getMessage();

            if (message.contains("java.time.LocalDate")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(
                                "INVALID_DATE_FORMAT",
                                HttpStatus.BAD_REQUEST.value(),
                                "날짜 형식이 올바르지 않습니다. yyyy-MM-dd 형식이어야 합니다."
                        ));
            }

            if (message.contains("java.time.LocalTime")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(
                                "INVALID_TIME_FORMAT",
                                HttpStatus.BAD_REQUEST.value(),
                                "시간 형식이 올바르지 않습니다. HH:mm 형식이어야 합니다."
                        ));
            }
        }

        if (cause instanceof JsonParseException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(
                            "MALFORMED_JSON",
                            HttpStatus.BAD_REQUEST.value(),
                            "요청 본문 JSON 형식이 올바르지 않습니다."
                    ));
        }

        if (cause instanceof MismatchedInputException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(
                            "INVALID_TYPE_VALUE",
                            HttpStatus.BAD_REQUEST.value(),
                            "요청 값의 타입이 올바르지 않습니다."
                    ));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "INVALID_INPUT",
                        HttpStatus.BAD_REQUEST.value(),
                        "요청 형식이 올바르지 않습니다."
                ));
    }

    private String resolveValidationCode(final FieldError fieldError) {
        if ("typeMismatch".equals(fieldError.getCode())) {
            return resolveBindingCode(fieldError);
        }

        String objectName = fieldError.getObjectName();
        String field = fieldError.getField();

        if ("reservationCreateRequest".equals(objectName)) {
            return switch (field) {
                case "name" -> "INVALID_RESERVATION_NAME";
                case "date" -> "RESERVATION_DATE_REQUIRED";
                case "themeId" -> "THEME_ID_REQUIRED";
                case "timeId" -> "RESERVATION_TIME_ID_REQUIRED";
                default -> "INVALID_INPUT";
            };
        }

        if ("themeCreateRequest".equals(objectName) && "name".equals(field)) {
            return "INVALID_THEME_NAME";
        }

        if ("reservationTimeCreateRequest".equals(objectName) && "startAt".equals(field)) {
            return "RESERVATION_TIME_REQUIRED";
        }

        if ("availableReservationTimeRequest".equals(objectName) && "date".equals(field)) {
            return "RESERVATION_DATE_REQUIRED";
        }

        return "INVALID_INPUT";
    }

    private String resolveBindingCode(final FieldError fieldError) {
        if ("date".equals(fieldError.getField())) {
            return "INVALID_DATE_FORMAT";
        }

        if ("startAt".equals(fieldError.getField())) {
            return "INVALID_TIME_FORMAT";
        }

        return "INVALID_TYPE_VALUE";
    }

    private String resolveTypeMismatchCode(final Class<?> targetType) {
        if (Objects.isNull(targetType)) {
            return "INVALID_TYPE_VALUE";
        }

        if (LocalDate.class.equals(targetType)) {
            return "INVALID_DATE_FORMAT";
        }

        if (LocalTime.class.equals(targetType)) {
            return "INVALID_TIME_FORMAT";
        }

        if (targetType.isEnum()) {
            return "INVALID_ENUM_VALUE";
        }

        return "INVALID_TYPE_VALUE";
    }

    private String resolveTypeMismatchMessage(final Class<?> targetType) {
        if (Objects.isNull(targetType)) {
            return "요청 값의 타입이 올바르지 않습니다.";
        }

        if (LocalDate.class.equals(targetType)) {
            return "날짜 형식이 올바르지 않습니다. yyyy-MM-dd 형식이어야 합니다.";
        }

        if (LocalTime.class.equals(targetType)) {
            return "시간 형식이 올바르지 않습니다. HH:mm 형식이어야 합니다.";
        }

        if (targetType.isEnum()) {
            return "허용되지 않는 요청 값입니다.";
        }

        return "요청 값의 타입이 올바르지 않습니다.";
    }
}
