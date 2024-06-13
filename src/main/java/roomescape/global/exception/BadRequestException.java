package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends StatusException {

    private static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;

    private final String message;

    public BadRequestException(String message) {
        super(BAD_REQUEST, message);
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
