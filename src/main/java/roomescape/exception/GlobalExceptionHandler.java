package roomescape.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException exception) {
        log.warn("Custom Error: ", exception);
        return ResponseEntity
                .status(exception.getStatus())
                .body(new ErrorResponseDto(exception.getStatus(), exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleRequestValidException(MethodArgumentNotValidException exception) {
        log.warn("Request Valid Error: ", exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(HttpStatus.BAD_REQUEST, exception.getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleDomainValidException(IllegalArgumentException exception) {
        log.warn("Domain Valid Error: ", exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleOtherException(Exception exception) {
        log.error("Internal Server Error: ", exception);
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(new ErrorResponseDto(ErrorCode.INTERNAL_SERVER_ERROR.getStatus(),
                        ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }
}
