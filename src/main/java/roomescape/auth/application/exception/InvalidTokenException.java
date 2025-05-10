package roomescape.auth.application.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends RuntimeException {
    private final HttpStatus statusCode;

    public InvalidTokenException(HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
