package roomescape.exception;

public class NotEnoughPermissionException extends BadRequestException {
    public NotEnoughPermissionException(String message) {
        super(message);
    }
}
