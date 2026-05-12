package roomescape.exception;

import org.springframework.http.HttpStatus;

public class DuplicateException extends ApiException {
    public DuplicateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
