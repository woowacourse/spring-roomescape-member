package roomescape.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedActionException extends ApiException {
    public UnauthorizedActionException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
