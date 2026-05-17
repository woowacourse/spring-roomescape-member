package roomescape.exception;

import lombok.Getter;

@Getter
public class DuplicatedResourceException extends RuntimeException {

    private final String code;

    public DuplicatedResourceException(String message, String code) {
        super(message);
        this.code = code;
    }
}
