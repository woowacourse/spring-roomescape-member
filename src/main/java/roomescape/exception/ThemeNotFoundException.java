package roomescape.exception;

public class ThemeNotFoundException extends RuntimeException {

    public ThemeNotFoundException() {
        super("요청 받은 ID에 해당하는 테마가 존재하지 않습니다.");
    }
}
