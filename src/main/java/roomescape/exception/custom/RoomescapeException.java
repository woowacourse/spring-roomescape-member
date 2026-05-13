package roomescape.exception.custom;

import org.springframework.http.HttpStatus;

public abstract class RoomescapeException extends RuntimeException {

    private final HttpStatus httpStatus;

    public RoomescapeException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
