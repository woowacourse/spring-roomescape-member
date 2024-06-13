package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends StatusException {

    private static final HttpStatus UNAUTHORIZED = HttpStatus.UNAUTHORIZED;

    private final String message;

    public UnAuthorizedException(String message) {
        super(UNAUTHORIZED, message);
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return UNAUTHORIZED;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
