package roomescape.exception;

import org.springframework.http.HttpStatus;

public class CustomForbidden extends CustomException {

    public CustomForbidden(final String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
