package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public class UnprocessableException extends RuntimeException {
    private final HttpStatus httpStatus;

    public UnprocessableException(String message) {
        super(message);
        this.httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}
