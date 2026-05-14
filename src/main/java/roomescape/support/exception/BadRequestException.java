package roomescape.support.exception;

public class BadRequestException extends RoomescapeException {

    public BadRequestException(Errors errors, Object... args) {
        super(errors, args);
    }
}
