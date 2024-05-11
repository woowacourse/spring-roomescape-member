package roomescape.service.auth.exception;

import roomescape.exception.UnauthorizedException;

public class TokenNotFoundException extends UnauthorizedException {

    public TokenNotFoundException(final String message) {
        super(message);
    }
}
