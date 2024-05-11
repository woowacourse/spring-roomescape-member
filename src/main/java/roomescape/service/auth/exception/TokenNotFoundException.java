package roomescape.service.auth.exception;

import roomescape.exception.UnauthorizedException;

public class TokenNotFoundException extends UnauthorizedException {

    public TokenNotFoundException(String message) {
        super(message);
    }
}
