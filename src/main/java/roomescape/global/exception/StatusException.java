package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class StatusException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public StatusException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
