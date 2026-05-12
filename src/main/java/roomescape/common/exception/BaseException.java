package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    private final HttpStatus status;

    public BaseException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    public BaseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
