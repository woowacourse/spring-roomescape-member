package roomescape.global;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.ErrorResponse;
import roomescape.global.exception.RoomescapeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = "";

        if (ex.getBindingResult().hasErrors()) {
            message = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        }
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_NAME_FORMAT.name(), message);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_DATE_FORMAT.name(),
                ErrorCode.INVALID_DATE_FORMAT.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RoomescapeException.class)
    protected ResponseEntity<ErrorResponse> handleRoomescapeException(RoomescapeException ex) {
        ErrorCode error = ex.getErrorCode();

        return ResponseEntity.status(error.getStatus()).body(ErrorResponse.of(error.name(), error.getMessage()));
    }
}
