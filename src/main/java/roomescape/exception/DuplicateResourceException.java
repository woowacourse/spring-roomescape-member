package roomescape.exception;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends RoomescapeException {

    public DuplicateResourceException(String message) {
        super(HttpStatus.CONFLICT, "DUPLICATE_RESOURCE", message);
    }
}
