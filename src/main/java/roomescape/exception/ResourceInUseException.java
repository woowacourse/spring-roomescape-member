package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ResourceInUseException extends ApiException {
    public ResourceInUseException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
