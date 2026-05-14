package roomescape.exception;

import lombok.Getter;

@Getter
public abstract class CodeException extends RuntimeException {

    private final ErrorCode errorCode;

    public CodeException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
