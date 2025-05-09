package roomescape.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(errorCode.getStatus())
                .body(errorCode.getMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();

        if (cause instanceof InvalidFormatException invalidFormatException) {
            return handleInvalidFormatException(invalidFormatException);
        }
        return ResponseEntity.badRequest().body("[ERROR] 요청 형식이 잘못되었습니다.");
    }

    private ResponseEntity<String> handleInvalidFormatException(InvalidFormatException e) {
        String field = e.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("."));

        String expectedType = e.getTargetType().getSimpleName();

        return ResponseEntity.badRequest()
                .body("[ERROR] 필드 [" + field + "]의 값은 " + expectedType + " 형식이어야 합니다.");
    }
}
