package roomescape.handler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.dto.ErrorResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleIllegalArgumentException(MethodArgumentNotValidException e,
                                                                              HttpServletRequest request) {
        List<ErrorResponse> errors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            ErrorCode error = ErrorCode.valueOf(fieldError.getDefaultMessage());
            ErrorResponse errorResponse = new ErrorResponse(
                    error.getCode(), request.getRequestURI(), error.getMessage(), error.getAction()
            );
            errors.add(errorResponse);
        });
        return ResponseEntity.status(e.getStatusCode()).body(errors);
    }

    @ExceptionHandler(RoomescapeException.class)
    public ResponseEntity<ErrorResponse> handleRoomescapeException(RoomescapeException e, HttpServletRequest request) {
        ErrorCode error = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(
                error.getCode(), request.getRequestURI(), error.getMessage(), error.getAction()
        );
        return ResponseEntity.status(error.getStatus()).body(errorResponse);
    }
}
