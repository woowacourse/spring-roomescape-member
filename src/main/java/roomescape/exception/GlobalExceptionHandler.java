package roomescape.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                "RESOURCE_NOT_FOUND",
                request.getRequestURI(),
                e.getMessage(),
                "요청한 리소스의 식별자를 다시 확인해주세요."
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(DuplicationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicationException(DuplicationException e, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                "RESOURCE_DUPLICATED",
                request.getRequestURI(),
                e.getMessage(),
                e.getAction()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                "INVALID_REQUEST",
                request.getRequestURI(),
                e.getMessage(),
                "요청 값을 확인 후 다시 시도해주세요."
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = "요청 형식이 올바르지 않습니다.";
        if (fieldError != null) {
            message = fieldError.getField() + ": " + fieldError.getDefaultMessage();
        }
        ErrorResponse body = new ErrorResponse(
                "VALIDATION_FAILED",
                request.getRequestURI(),
                message,
                "요청 값을 확인 후 다시 시도해주세요."
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                request.getRequestURI(),
                "요청 처리에 문제가 발생했습니다.",
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
