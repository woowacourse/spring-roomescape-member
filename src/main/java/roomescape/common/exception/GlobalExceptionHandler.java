package roomescape.common.exception;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, exception.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception
    ) {
        ErrorCode errorCode = ErrorCode.DATA_CONFLICT;

        log.warn("데이터 충돌 예외가 발생했습니다. errorCode={}", errorCode, exception);

        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, errorCode.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception exception) {
        log.error("예상하지 못한 서버 예외가 발생했습니다.", exception);

        return ResponseEntity.internalServerError().build();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception
    ) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(",\n"));

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_RESERVATION_TIME, message);

        return ResponseEntity.badRequest()
                .body(errorResponse);
    }
}
