package roomescape.exception;

import org.springframework.http.HttpStatus;

public class RoomescapeException extends RuntimeException {

    HttpStatus httpStatus;

    public RoomescapeException(final String message) {
        super(message);
    }

    public RoomescapeException(HttpStatus httpStatus) {
        this.httpStatus= httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
