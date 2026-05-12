package roomescape.global.exception.exception;

import lombok.Getter;

@Getter
public class DuplicateException extends RuntimeException {
    public DuplicateException(String message) {
        super(message);
    }
}
