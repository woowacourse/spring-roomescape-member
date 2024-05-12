package roomescape.exception;

import org.springframework.http.HttpStatus;

public class CustomUnauthorized extends CustomException2 {

    private CustomUnauthorized(final String message, final HttpStatus status) {
        super(message, status);
    }

    public CustomUnauthorized(final String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
