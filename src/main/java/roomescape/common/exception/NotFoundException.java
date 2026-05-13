package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException {
    private final HttpStatus httpStatus;

    public NotFoundException(String message) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}
