package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistException extends RuntimeException {
    private final HttpStatus httpStatus;

    public AlreadyExistException(String message) {
        super(message);
        this.httpStatus = HttpStatus.CONFLICT;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}
