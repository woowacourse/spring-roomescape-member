package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RoomescapeException {

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", message);
    }
}
