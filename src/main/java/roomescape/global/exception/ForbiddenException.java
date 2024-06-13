package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends StatusException {

    private static final HttpStatus FORBIDDEN = HttpStatus.FORBIDDEN;

    private final String message;

    public ForbiddenException(String message) {
        super(FORBIDDEN, message);
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return FORBIDDEN;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
