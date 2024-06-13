package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends RuntimeException {

    private static final HttpStatus httpStatus = HttpStatus.FORBIDDEN;

    private final String message;

    public ForbiddenException(String message) {
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
