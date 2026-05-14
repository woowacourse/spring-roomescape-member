package roomescape.theme.exception;

import java.util.List;
import roomescape.global.exception.exception.InvalidException;

public class ThemeInvalidException extends InvalidException {
    public ThemeInvalidException(List<String> errors) {
        super(errors);
    }
}
