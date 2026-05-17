package roomescape.global.exception.exception;

import java.util.List;
import lombok.Getter;
import roomescape.global.exception.GlobalErrorCode;

@Getter
public class ValidationException extends RuntimeException {

    private final List<String> errors;

    public ValidationException(List<String> errors) {
        super(GlobalErrorCode.INVALID_INPUT.getMessage());
        this.errors = errors;
    }

}
