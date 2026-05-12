package roomescape.exception.exception;

import lombok.Getter;

@Getter
public abstract class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
