package roomescape.exception;

public class NotExistingEntryException extends IllegalArgumentException {

    public NotExistingEntryException(String message) {
        super(message);
    }
}
