package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicateException extends RuntimeException {
    private final HttpStatus httpStatus;

    public DuplicateException(String message) {
        super(message);
        this.httpStatus = HttpStatus.CONFLICT;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}
