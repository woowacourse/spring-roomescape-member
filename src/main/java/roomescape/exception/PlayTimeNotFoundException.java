package roomescape.exception;

import java.util.NoSuchElementException;

public class PlayTimeNotFoundException extends NoSuchElementException {

    private static final String DEFAULT_MESSAGE = "해당하는 방탈출 시간 id를 찾을 수 없습니다. id : ";

    public PlayTimeNotFoundException(final String message) {
        super(message);
    }

    public PlayTimeNotFoundException(final Long id) {
        super(DEFAULT_MESSAGE + id);
    }
}
