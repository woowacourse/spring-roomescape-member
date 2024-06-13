package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends RuntimeException {

    private static final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    private final String message;

    public UnAuthorizedException(String message) {
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
