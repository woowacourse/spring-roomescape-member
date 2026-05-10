package roomescape.global.exception;

public class ThemeNotFoundException extends RoomescapeException {

    public ThemeNotFoundException() {
        super(ErrorCode.THEME_NOT_FOUND, "존재하지 않는 테마입니다.");
    }

    public ThemeNotFoundException(String message) {
        super(ErrorCode.THEME_NOT_FOUND, message);
    }
}
