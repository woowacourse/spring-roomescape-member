package roomescape.exception;

import org.springframework.http.HttpStatus;

public class CustomBadRequest extends CustomException {

    public CustomBadRequest(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
