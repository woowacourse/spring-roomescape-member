package roomescape.exception;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {

    private static final String DEFAULT_MESSAGE = "해당하는 유저 id를 찾을 수 없습니다. id : ";

    public UserNotFoundException(final String message) {
        super(message);
    }

    public UserNotFoundException(final Long id) {
        super(DEFAULT_MESSAGE + id);
    }
}
