package roomescape.exception.exception;

import lombok.Getter;

@Getter
public abstract class DuplicateException extends RuntimeException {
    public DuplicateException(String message) {
        super(message);
    }
}
