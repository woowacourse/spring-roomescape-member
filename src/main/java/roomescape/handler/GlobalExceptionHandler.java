package roomescape.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import roomescape.dto.ErrorResponse;
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
}
