package roomescape.theme.exception;

public class ThemeInUseException extends RuntimeException {

    public static final String MESSAGE = "예약이 존재하는 테마는 삭제할 수 없습니다.";

    public ThemeInUseException(Long id) {
        super(MESSAGE + " id=" + id);
    }

}
