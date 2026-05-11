package roomescape.exception;

public class UnauthorizedException extends BaseCustomException {
    public UnauthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
