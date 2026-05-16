package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends CustomException {

    public EntityNotFoundException(String message) {
        super("ENTITY_NOT_FOUND", HttpStatus.NOT_FOUND, message);
    }
}
