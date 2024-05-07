package roomescape.exception;

public class DuplicatedThemeException extends RuntimeException {

    public DuplicatedThemeException() {
        super("같은 이름의 테마는 추가할 수 없습니다.");
    }
}
