package roomescape.global.exception.exception;

import lombok.Getter;

@Getter
public class ResourceInUseException extends RuntimeException {
    public ResourceInUseException(String message) {
        super(message);
    }
}
