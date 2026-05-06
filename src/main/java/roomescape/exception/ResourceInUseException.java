package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ResourceInUseException extends ApiException {
    private final HttpStatus status = HttpStatus.CONFLICT;

    public ResourceInUseException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
