package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public ApiException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
