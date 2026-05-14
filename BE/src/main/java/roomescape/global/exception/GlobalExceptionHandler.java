package roomescape.global.exception;

import static org.h2.expression.function.DateTimeFunction.getFieldName;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import roomescape.global.exception.customException.RoomEscapeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoomEscapeException.class)
    public ResponseEntity<ErrorResponse> handleRoomEscape(RoomEscapeException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestBody(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException invalidFormatException) {
            String fieldName = getFieldName(invalidFormatException);
            String typeHint = getTypeHint(invalidFormatException.getTargetType());

            String message = "%s 값의 형식이 올바르지 않습니다. %s 형식으로 입력해 주세요."
                    .formatted(fieldName, typeHint);

            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(message));
        }

        return createErrorResponse(CommonErrorCode.INVALID_REQUEST_BODY);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String message = "%s 값의 형식이 올바르지 않습니다. %s 형식으로 입력해 주세요."
                .formatted(e.getName(), getTypeHint(e.getRequiredType()));

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return createErrorResponse(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getMessage()));
    }

    private String getFieldName(InvalidFormatException e) {
        return e.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("요청 값");
    }

    private String getTypeHint(Class<?> type) {
        if (type == null) {
            return "올바른";
        }

        if (type.equals(LocalDate.class)) {
            return "yyyy-MM-dd";
        }

        if (type.equals(LocalTime.class)) {
            return "HH:mm";
        }

        if (type.equals(Long.class) || type.equals(long.class)) {
            return "숫자";
        }

        if (type.equals(Integer.class) || type.equals(int.class)) {
            return "숫자";
        }

        return type.getSimpleName();
    }

}
