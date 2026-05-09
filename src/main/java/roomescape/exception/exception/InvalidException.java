package roomescape.exception.exception;

import java.util.List;
import lombok.Getter;
import roomescape.exception.GlobalErrorCode;

@Getter
public abstract class InvalidException extends RuntimeException {

    private final List<String> errors;

    public InvalidException(List<String> errors) {
        super(GlobalErrorCode.INVALID_INPUT.getMessage());
        this.errors = errors;
    }

}
