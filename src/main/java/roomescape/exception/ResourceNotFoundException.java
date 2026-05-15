package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RoomEscapeException {

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
