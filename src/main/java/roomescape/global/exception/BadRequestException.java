package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException {

    private static final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    private final String message;

    public BadRequestException(String message) {
        super(message);
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
