package roomescape.exception;

import org.springframework.http.HttpStatus;

public class CustomForbidden extends CustomException2 {

    private CustomForbidden(final String message, final HttpStatus status) {
        super(message, status);
    }

    public CustomForbidden(final String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
