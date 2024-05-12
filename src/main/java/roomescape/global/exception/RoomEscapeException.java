package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class RoomEscapeException extends RuntimeException {

    private final HttpStatus httpStatus;

    public RoomEscapeException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
