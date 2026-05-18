package roomescape.exception;

import org.springframework.http.HttpStatus;

public abstract class RoomescapeException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    protected RoomescapeException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
