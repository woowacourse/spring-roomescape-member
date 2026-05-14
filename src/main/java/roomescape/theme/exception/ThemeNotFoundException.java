package roomescape.theme.exception;

public class ThemeNotFoundException extends RuntimeException {

    public static final String MESSAGE = "존재하지 않는 테마입니다.";

    public ThemeNotFoundException(Long id) {
        super(MESSAGE + " id=" + id);
    }
    
}
