package roomescape.theme.exception;

import java.util.List;
import roomescape.global.exception.exception.ValidationException;

public class ThemeValidationException extends ValidationException {
    public ThemeValidationException(List<String> errors) {
        super(errors);
    }
}
