package roomescape.theme.exception;

import roomescape.global.exception.EntityDuplicateException;

public class ThemeDuplicateException extends EntityDuplicateException {

    public ThemeDuplicateException(String message, String name) {
        super("[ERROR] " + message + " " + name);
    }
}
