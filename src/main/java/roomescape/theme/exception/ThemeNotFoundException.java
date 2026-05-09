package roomescape.theme.exception;

public class ThemeNotFoundException extends RuntimeException {

    public ThemeNotFoundException(Long id) {
        super("존재하지 않는 테마입니다. id=" + id);
    }
    
}
