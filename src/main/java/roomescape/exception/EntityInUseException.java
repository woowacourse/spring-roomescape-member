package roomescape.exception;

public class EntityInUseException extends ApiException {
    public EntityInUseException(ErrorCode code, String message) {
        super(code, message);
    }
}