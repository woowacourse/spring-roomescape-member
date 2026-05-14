package roomescape.support.exception;

public class InternalServerException extends RoomescapeException {

    public InternalServerException(Errors errors, Object... args) {
        super(errors, args);
    }
}
