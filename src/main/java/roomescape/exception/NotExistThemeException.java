package roomescape.exception;

public class NotExistThemeException extends NotExistException {

    public NotExistThemeException(final long id) {
        super(String.format("테마 ID %d에 해당하는 값이 없습니다", id));
    }
}
