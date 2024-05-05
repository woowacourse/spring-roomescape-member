package roomescape.exception;

public class ReferencedRowExistsException extends IllegalArgumentException {

    public ReferencedRowExistsException(String message) {
        super(message);
    }
}
