package roomescape.exception;

import jakarta.servlet.http.HttpServletRequest;
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
        return toResponse(e.getCode(), request, e.getMessage());
    }

    @ExceptionHandler(DuplicationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicationException(DuplicationException e, HttpServletRequest request) {
        return toResponse(e.getCode(), request, e.getMessage());
    }

    @ExceptionHandler(UnprocessableException.class)
    public ResponseEntity<ErrorResponse> handleUnprocessableException(UnprocessableException e, HttpServletRequest request) {
        return toResponse(e.getCode(), request, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        ErrorCode code = ErrorCode.INVALID_REQUEST;
        String message = e.getMessage();
        if (message == null || message.isBlank()) {
            message = code.getMessage();
        }
        return toResponse(code, request, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        ErrorCode code = ErrorCode.VALIDATION_FAILED;
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = code.getMessage();
        if (fieldError != null) {
            message = fieldError.getField() + ": " + fieldError.getDefaultMessage();
        }
        return toResponse(code, request, message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e, HttpServletRequest request) {
        ErrorCode code = ErrorCode.INTERNAL_SERVER_ERROR;
        return toResponse(code, request, code.getMessage());
    }

    private ResponseEntity<ErrorResponse> toResponse(ErrorCode code, HttpServletRequest request, String message) {
        ErrorResponse body = new ErrorResponse(
                code.name(),
                request.getRequestURI(),
                message,
                code.getAction()
        );
        return ResponseEntity.status(code.getStatus()).body(body);
    }
}
