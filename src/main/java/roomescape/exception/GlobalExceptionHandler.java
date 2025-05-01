package roomescape.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<ErrorResponse> handleJsonParseError(
            final HttpMessageNotReadableException e,
            final HttpServletRequest request
    ) {
        String path = request.getServletPath();
        if (request.getQueryString() != null) {
            path += "?" + request.getQueryString();
        }
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "요청 형식이 올바르지 않습니다.",
                path
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponse> handleMethodArgumentException(
            final MethodArgumentNotValidException e,
            final HttpServletRequest request
    ) {
        String path = request.getServletPath();
        if (request.getQueryString() != null) {
            path += "?" + request.getQueryString();
        }
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getBindingResult().getFieldError().getDefaultMessage(),
                path
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            final IllegalArgumentException e,
            final HttpServletRequest request
    ) {
        String path = request.getServletPath();
        if (request.getQueryString() != null) {
            path += "?" + request.getQueryString();
        }
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                path
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
