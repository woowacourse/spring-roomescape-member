package roomescape.exception;

import org.springframework.http.HttpStatus;

public class CustomBadRequest extends CustomException2 {

    private CustomBadRequest(final String message, final HttpStatus status) {
        super(message, status);
    }

    public CustomBadRequest(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
