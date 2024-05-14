package roomescape.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {
    public NotFoundException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
