package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedException extends CustomException {
    public DuplicatedException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
