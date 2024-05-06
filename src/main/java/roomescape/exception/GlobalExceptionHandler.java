package roomescape.exception;

import java.time.format.DateTimeParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {InvalidInputException.class,
            ExistingEntryException.class,
            NotExistingEntryException.class,
            ReferencedRowExistsException.class,
            ReservingPastTimeException.class,
            NullPointerException.class
    })
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        if (e.getRootCause() instanceof DateTimeParseException exception) {
            String message = exception.getParsedString() + "은(는) 올바른 시간 형식이 아닙니다.";
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(message));
        }
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("올바르지 않은 형식입니다."));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("서버 에러입니다."));
    }
}
