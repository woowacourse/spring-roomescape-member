package roomescape.exception;

public class EntityInUseException extends ApiException {
    public EntityInUseException(String code, String message) {
        super(code, message);
    }
}