package roomescape.exceptions.reservation;

import roomescape.exceptions.EntityDuplicateException;

public class ThemeDuplicateException extends EntityDuplicateException {

    public ThemeDuplicateException(String message, String name) {
        super(message + " " + name);
    }
}
