package roomescape.global.exception.conflict;

public class ThemeNameConflictException extends ConflictException  {
    public ThemeNameConflictException() {
        super("테마 명");
    }
}
