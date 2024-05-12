package roomescape.exception;

import org.springframework.http.HttpStatus;

public class CustomUnauthorized extends CustomException {

    public CustomUnauthorized(final String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
