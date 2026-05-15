package roomescape.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String VALIDATION_MESSAGE_SEPARATOR = "::";

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(final ApiException exception) {
        return errorResponse(exception.getStatus(), exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException exception
    ) {
        FieldError fieldError = findFirstFieldError(exception);
        return buildValidationErrorResponse(fieldError);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(final BindException exception) {
        FieldError fieldError = findFirstFieldError(exception);
        return buildBindingErrorResponse(fieldError);
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

        return badRequest(code, message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException exception
    ) {
        return badRequest(
                resolveMissingParameterCode(exception.getParameterName()),
                resolveMissingParameterMessage(exception.getParameterName())
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(final NoResourceFoundException exception) {
        return errorResponse(
                HttpStatus.NOT_FOUND,
                "RESOURCE_NOT_FOUND",
                "요청한 리소스를 찾을 수 없습니다."
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            final DataIntegrityViolationException exception
    ) {
        return errorResponse(
                HttpStatus.CONFLICT,
                "DATA_CONFLICT",
                "저장 중 충돌이 발생했습니다. 잠시 후 다시 시도해 주세요."
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        return errorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "서버 내부 오류가 발생했습니다."
        );
    }

    private ResponseEntity<ErrorResponse> buildHttpMessageNotReadableResponse(
            final HttpMessageNotReadableException exception
    ) {
        Throwable cause = exception.getMostSpecificCause();

        if (cause instanceof InvalidFormatException invalidFormatException) {
            Class<?> targetType = invalidFormatException.getTargetType();

            return badRequest(
                    resolveTypeMismatchCode(targetType),
                    resolveTypeMismatchMessage(targetType)
            );
        }

        if (cause instanceof DateTimeParseException) {
            String message = exception.getMessage();

            if (message.contains("java.time.LocalDate")) {
                return badRequest(
                        "INVALID_DATE_FORMAT",
                        "날짜 형식이 올바르지 않습니다. yyyy-MM-dd 형식이어야 합니다."
                );
            }

            if (message.contains("java.time.LocalTime")) {
                return badRequest(
                        "INVALID_TIME_FORMAT",
                        "시간 형식이 올바르지 않습니다. HH:mm 형식이어야 합니다."
                );
            }
        }

        if (cause instanceof JsonParseException) {
            return badRequest("MALFORMED_JSON", "요청 본문 JSON 형식이 올바르지 않습니다.");
        }

        if (cause instanceof MismatchedInputException) {
            return badRequest("INVALID_TYPE_VALUE", "요청 값의 타입이 올바르지 않습니다.");
        }

        return badRequest("INVALID_INPUT", "요청 형식이 올바르지 않습니다.");
    }

    private ResponseEntity<ErrorResponse> buildValidationErrorResponse(final FieldError fieldError) {
        if (Objects.isNull(fieldError)) {
            return badRequest("INVALID_INPUT", "유효하지 않은 입력입니다.");
        }

        ValidationErrorDetail validationErrorDetail = resolveValidationErrorDetail(fieldError);
        return badRequest(validationErrorDetail.code(), validationErrorDetail.message());
    }

    private ResponseEntity<ErrorResponse> buildBindingErrorResponse(final FieldError fieldError) {
        if (Objects.isNull(fieldError)) {
            return badRequest("INVALID_INPUT", "유효하지 않은 입력입니다.");
        }

        if ("typeMismatch".equals(fieldError.getCode())) {
            return badRequest(resolveBindingCode(fieldError), fieldError.getDefaultMessage());
        }

        ValidationErrorDetail validationErrorDetail = resolveValidationErrorDetail(fieldError);
        return badRequest(validationErrorDetail.code(), validationErrorDetail.message());
    }

    private FieldError findFirstFieldError(final MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .orElse(null);
    }

    private FieldError findFirstFieldError(final BindException exception) {
        return exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .orElse(null);
    }

    private ResponseEntity<ErrorResponse> badRequest(final String code, final String message) {
        return errorResponse(HttpStatus.BAD_REQUEST, code, message);
    }

    private ResponseEntity<ErrorResponse> errorResponse(
            final HttpStatus status,
            final String code,
            final String message
    ) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(code, status.value(), message));
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

    private ValidationErrorDetail resolveValidationErrorDetail(final FieldError fieldError) {
        String defaultMessage = fieldError.getDefaultMessage();

        if (Objects.isNull(defaultMessage) || !defaultMessage.contains(VALIDATION_MESSAGE_SEPARATOR)) {
            if (Objects.isNull(defaultMessage)) {
                return new ValidationErrorDetail("INVALID_INPUT", "유효하지 않은 입력입니다.");
            }

            return new ValidationErrorDetail("INVALID_INPUT", defaultMessage);
        }

        String[] parts = defaultMessage.split(VALIDATION_MESSAGE_SEPARATOR, 2);
        return new ValidationErrorDetail(parts[0], parts[1]);
    }

    private String resolveMissingParameterCode(final String parameterName) {
        if ("name".equals(parameterName)) {
            return "RESERVATION_NAME_REQUIRED";
        }

        if ("date".equals(parameterName)) {
            return "RESERVATION_DATE_REQUIRED";
        }

        if ("themeId".equals(parameterName)) {
            return "THEME_ID_REQUIRED";
        }

        if ("timeId".equals(parameterName)) {
            return "RESERVATION_TIME_ID_REQUIRED";
        }

        if ("startAt".equals(parameterName)) {
            return "RESERVATION_TIME_REQUIRED";
        }

        return "INVALID_INPUT";
    }

    private String resolveMissingParameterMessage(final String parameterName) {
        if ("name".equals(parameterName)) {
            return "예약자 이름은 비어 있을 수 없습니다.";
        }

        if ("date".equals(parameterName)) {
            return "날짜는 필수입니다.";
        }

        if ("themeId".equals(parameterName)) {
            return "themeId는 필수입니다.";
        }

        if ("timeId".equals(parameterName)) {
            return "timeId는 필수입니다.";
        }

        if ("startAt".equals(parameterName)) {
            return "예약 시간은 필수입니다.";
        }

        return "유효하지 않은 입력입니다.";
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
