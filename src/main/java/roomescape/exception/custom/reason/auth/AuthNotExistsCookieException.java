package roomescape.exception.custom.reason.auth;

import roomescape.exception.custom.status.UnauthorizedException;

public class AuthNotExistsCookieException extends UnauthorizedException {

    public AuthNotExistsCookieException() {
        super("쿠키가 존재하지 않습니다.");
    }
}
