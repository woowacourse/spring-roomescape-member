package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RoomescapeExceptionHandler {
    private static class ErrorResponse {
        private static final boolean ERROR_STATUS = false;
        private final String message;

        public ErrorResponse(Exception e) {
            this.message = e.getMessage();
        }

        public boolean isStatus() {
            return ERROR_STATUS;
        }

        public String getMessage() {
            return message;
        }
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleArgumentException(final IllegalArgumentException exception) {
        return ResponseEntity.badRequest()
                             .body(new ErrorResponse(exception));
    }

    @ExceptionHandler(value = PastTimeReservationException.class)
    public ResponseEntity<ErrorResponse> handlePastTimeReservationException(final PastTimeReservationException exception) {
        return ResponseEntity.badRequest()
                             .body(new ErrorResponse(exception));
    }


    @ExceptionHandler(value = ExistReservationException.class)
    public ResponseEntity<ErrorResponse> handleExistReservationException(final ExistReservationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(new ErrorResponse(exception));
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExistsException(final AlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(new ErrorResponse(exception));

    }

    @ExceptionHandler(value = NotExistException.class)
    public ResponseEntity<ErrorResponse> handleNotExistException(final NotExistException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ErrorResponse(exception));
    }
}
