package roomescape.exception;

public class EntityInUseException extends ApiException {
    public EntityInUseException(String message) {
        super(message);
    }
}