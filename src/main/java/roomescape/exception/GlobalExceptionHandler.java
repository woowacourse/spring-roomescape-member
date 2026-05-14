package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.common.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<?>> handleBaseException(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(resolveHttpStatus(errorCode))
                .body(ApiResponse.fail(errorCode.getCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(resolveHttpStatus(errorCode)).body(ApiResponse.fail(errorCode.getCode(), errorCode.getMessage()));
    }

    private HttpStatus resolveHttpStatus(ErrorCode errorCode) {
        String code = errorCode.getCode();
        if (code.contains("400")) {
            return HttpStatus.BAD_REQUEST;
        }
        if (code.contains("404")) {
            return HttpStatus.NOT_FOUND;
        }
        if (code.contains("409")) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
