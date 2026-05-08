package roomescape.theme.exception;

import java.util.List;
import java.util.Map;
import roomescape.exception.exception.InvalidException;

public class InValidThemeException extends InvalidException {
    public InValidThemeException(List<String> errors) {
        super(errors);
    }
}
