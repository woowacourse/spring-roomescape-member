package roomescape.service.auth.exception;

import roomescape.exception.BadRequestException;

// TODO: Split service package and locate exceptions
public class TokenNotFoundException extends BadRequestException {

    public TokenNotFoundException(final String message) {
        super(message);
    }
}
