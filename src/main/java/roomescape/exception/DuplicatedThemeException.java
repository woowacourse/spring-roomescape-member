package roomescape.exception;

public class DuplicatedThemeException extends RuntimeException {

    public DuplicatedThemeException() {
        super("이미 존재하는 테마는 추가할 수 없습니다.");
    }
}
