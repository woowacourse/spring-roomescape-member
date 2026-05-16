package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public abstract class RoomescapeException extends RuntimeException {

    private final HttpStatus status;

    protected RoomescapeException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
