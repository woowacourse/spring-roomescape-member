package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ReservationException extends RuntimeException {

    private final HttpStatus status;

    public ReservationException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
