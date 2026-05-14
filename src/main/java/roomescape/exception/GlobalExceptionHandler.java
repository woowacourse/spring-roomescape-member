package roomescape.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ErrorResponseDto> buildErrorResponse(ErrorCode errorCode) {
        HttpStatus status = errorCode.getStatus();
        String message = errorCode.getMessage();

        return ResponseEntity
                .status(status)
                .body(ErrorResponseDto.of(status, message));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException exception) {
        log.warn("Custom Error: ", exception);
        return ResponseEntity
                .status(exception.getStatus())
                .body(ErrorResponseDto.of(exception.getStatus(), exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidException(MethodArgumentNotValidException exception) {
        log.warn("Valid Error: ", exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.of(HttpStatus.BAD_REQUEST, exception.getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleNotReadableException(HttpMessageNotReadableException exception) {
        log.warn("Type Mismatch Error: ", exception);
        Throwable cause = exception.getCause();

        if (isLocalDateFormatException(cause)) {
            return buildErrorResponse(ErrorCode.INVALID_DATE_FORMAT);
        }

        return buildErrorResponse(ErrorCode.INVALID_REQUEST_FORMAT);
    }

    private boolean isLocalDateFormatException(Throwable cause) {
        if (!(cause instanceof InvalidFormatException invalidFormatException)) {
            return false;
        }

        Class<?> targetType = invalidFormatException.getTargetType();
        return LocalDate.class.equals(targetType);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        log.warn("Type Mismatch Error: ", exception);
        if (LocalDate.class.equals(exception.getRequiredType())) {
            return buildErrorResponse(ErrorCode.INVALID_DATE_FORMAT);
        }
        return buildErrorResponse(ErrorCode.INVALID_REQUEST_FORMAT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleOtherException(Exception exception) {
        log.error("Internal Server Error: ", exception);
        return buildErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
