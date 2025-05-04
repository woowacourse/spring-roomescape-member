package roomescape.common.exception;

public class NotAbleDeleteException extends InvalidRequestException {
    public NotAbleDeleteException() {
    }

    public NotAbleDeleteException(String message) {
        super(message);
    }
}
