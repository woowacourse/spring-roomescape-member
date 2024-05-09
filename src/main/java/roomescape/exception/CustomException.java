package roomescape.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private final HttpStatus status;

    public CustomException(final HttpStatus status, final String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
