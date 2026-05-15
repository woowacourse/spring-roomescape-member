package roomescape.global.exception.exception;

import lombok.Getter;

@Getter
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
