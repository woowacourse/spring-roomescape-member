package roomescape.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public NotFoundException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
