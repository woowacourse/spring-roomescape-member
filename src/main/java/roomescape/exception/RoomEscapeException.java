package roomescape.exception;

import org.springframework.http.HttpStatus;

public class RoomEscapeException extends RuntimeException {

    private final HttpStatus httpStatus;

    public RoomEscapeException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
