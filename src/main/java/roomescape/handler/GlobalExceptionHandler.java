package roomescape.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import roomescape.dto.ErrorResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RoomescapeException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(RoomescapeException e,
                                                                     HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse(e.getCode().toString(), request.getRequestURI(),
                e.getCode().getMessage(),
                e.getCode().getAction());

        return ResponseEntity.status(e.getCode().getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(Exception e, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        ErrorResponse response = new ErrorResponse(errorCode.toString(), request.getRequestURI(),
                errorCode.getMessage(), errorCode.getAction());
        return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getStatus()).body(response);
    }
}
