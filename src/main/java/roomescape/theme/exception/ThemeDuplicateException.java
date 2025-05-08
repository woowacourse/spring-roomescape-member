package roomescape.theme.exception;

import roomescape.common.exception.EntityDuplicateException;

public class ThemeDuplicateException extends EntityDuplicateException {

    public ThemeDuplicateException(String message, String name) {
        super("[ERROR] " + message + " " + name);
    }
}
