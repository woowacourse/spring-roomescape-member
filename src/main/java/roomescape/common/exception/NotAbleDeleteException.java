package roomescape.common.exception;

public class NotAbleDeleteException extends RuntimeException {
    public NotAbleDeleteException(String message) {
        super(message);
    }

    public NotAbleDeleteException() {
    }
}
