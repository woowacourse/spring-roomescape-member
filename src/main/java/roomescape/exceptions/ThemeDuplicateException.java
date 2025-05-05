package roomescape.exceptions;

public class ThemeDuplicateException extends EntityDuplicateException {

    public ThemeDuplicateException(String message, String name) {
        super(message + " " + name);
    }
}
