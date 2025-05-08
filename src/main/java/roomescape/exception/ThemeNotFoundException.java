package roomescape.exception;

import java.util.NoSuchElementException;

public class ThemeNotFoundException extends NoSuchElementException {

    private static final String DEFAULT_MESSAGE = "해당하는 테마 id를 찾을 수 없습니다. id : ";

    public ThemeNotFoundException(final String message) {
        super(message);
    }

    public ThemeNotFoundException(final Long id) {
        super(DEFAULT_MESSAGE + id);
    }
}
