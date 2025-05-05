package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
