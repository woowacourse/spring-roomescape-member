package roomescape.exception;

import org.springframework.http.HttpStatus;

public class RoomescapeBaseException extends RuntimeException {
    private final HttpStatus status;

    public RoomescapeBaseException(HttpStatus status, String message) {
        super(message);  // RuntimeException에 message를 위임
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
