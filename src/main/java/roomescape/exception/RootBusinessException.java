package roomescape.exception;

public abstract class RootBusinessException extends RuntimeException {

    public RootBusinessException(String message) {
        super(message);
    }
}
