package roomescape.exception;

public class DuplicatedException extends BadRequestException {
    public DuplicatedException(String message) {
        super(message);
    }
}
