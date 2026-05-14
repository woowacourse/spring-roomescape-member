package roomescape.support.exception;

public class NotFoundException extends RoomescapeException {

    public NotFoundException(Errors errors, Object... args) {
        super(errors, args);
    }
}
