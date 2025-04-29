package roomescape.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super("[ERROR] " + message);
    }
}
