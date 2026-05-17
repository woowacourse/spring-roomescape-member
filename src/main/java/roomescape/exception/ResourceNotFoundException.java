package roomescape.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final String code;

    public ResourceNotFoundException(String message, String code) {
        super(message);
        this.code = code;
    }
}
