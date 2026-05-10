package roomescape.exception;

public class BaseCustomException extends RuntimeException implements CustomException {
    private final ErrorMessage errorMessage;

    public BaseCustomException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    @Override
    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
