package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class InvalidReservationException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public InvalidReservationException(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return errorMessage.getMessage();
    }

    public HttpStatus getStatusCode() {
        return errorMessage.getHttpStatus();
    }
}
