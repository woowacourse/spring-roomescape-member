package roomescape.global.exception;

public class InvalidThemeException extends RoomescapeException {
    public InvalidThemeException(String message) {
        super(ErrorCode.INVALID_THEME, message);
    }
}
