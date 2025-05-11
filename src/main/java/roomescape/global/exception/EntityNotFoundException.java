package roomescape.global.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super("[ERROR] " + message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super("[ERROR] " + message);
    }
}
