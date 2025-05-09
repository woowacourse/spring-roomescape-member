package roomescape.user;

import org.springframework.http.HttpStatus;

public class InvalidUserException extends RuntimeException {

    private final HttpStatus statusCode;

    public InvalidUserException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
