package roomescape.exception;

public class NotFoundResourceException extends BaseCustomException {
    public NotFoundResourceException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
