package roomescape.theme.exception;

public class ThemeInUseException extends RuntimeException {

    public ThemeInUseException(Long id) {
        super("예약이 존재하는 테마는 삭제할 수 없습니다. id=" + id);
    }

}
