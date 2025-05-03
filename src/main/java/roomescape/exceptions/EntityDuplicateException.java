package roomescape.exceptions;

public class EntityDuplicateException extends RuntimeException {

    public EntityDuplicateException(String message) {
        super("[ERROR] " + message);
    }
}
