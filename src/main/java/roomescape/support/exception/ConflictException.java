package roomescape.support.exception;

public class ConflictException extends RoomescapeException {

    public ConflictException(Errors errors, Object... args) {
        super(errors, args);
    }
}
