package roomescape.domain.exception;

public class ThemeDuplicatedException extends IllegalArgumentException {

    private static final String message = "[ERROR] 해당 테마 이름이 이미 존재합니다.";

    public ThemeDuplicatedException() {
        super(message);
    }
}
