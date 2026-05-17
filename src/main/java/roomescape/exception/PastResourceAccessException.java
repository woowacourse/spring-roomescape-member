package roomescape.exception;

import lombok.Getter;

@Getter
public class PastResourceAccessException extends RuntimeException {

    private final String code;

    public PastResourceAccessException(String message, String code) {
        super(message);
        this.code = code;
    }
}
