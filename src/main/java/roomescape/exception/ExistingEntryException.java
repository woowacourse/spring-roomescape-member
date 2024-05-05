package roomescape.exception;

public class ExistingEntryException extends IllegalArgumentException {

    public ExistingEntryException(String message) {
        super(message);
    }
}
