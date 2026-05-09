package roomescape.exception.exception;

import lombok.Getter;

@Getter
public abstract class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
