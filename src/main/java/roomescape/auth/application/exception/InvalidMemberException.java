package roomescape.auth.application.exception;

import org.springframework.http.HttpStatus;

public class InvalidMemberException extends RuntimeException {

    private final HttpStatus statusCode;

    public InvalidMemberException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
