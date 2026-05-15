package roomescape.global.exception.exception;

import lombok.Getter;

@Getter
public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException(String message) {
        super(message);
    }
}
