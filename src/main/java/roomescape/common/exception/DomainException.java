package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public class DomainException extends BaseException {
    public DomainException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
