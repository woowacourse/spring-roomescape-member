package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RoomeScapeClientException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
