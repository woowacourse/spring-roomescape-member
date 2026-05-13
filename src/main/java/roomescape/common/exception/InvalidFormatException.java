package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidFormatException extends RuntimeException {
    private final HttpStatus httpStatus;

    public InvalidFormatException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}
