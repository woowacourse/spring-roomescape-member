package roomescape.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedActionException extends ApiException {
    private final HttpStatus status = HttpStatus.UNAUTHORIZED;

    public UnauthorizedActionException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
