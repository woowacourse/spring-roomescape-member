package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEntityException extends CustomException {

    public DuplicateEntityException(String message, Object... args) {
        super("DUPLICATE_ENTITY", HttpStatus.CONFLICT, message.formatted(args));
    }
}
