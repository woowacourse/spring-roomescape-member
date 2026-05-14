package roomescape.exception;

import org.springframework.http.HttpStatus;

public abstract class RoomescapeException extends RuntimeException {

    private final HttpStatus status;

    public RoomescapeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
