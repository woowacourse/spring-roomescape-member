package roomescape.service.auth.exception;

import roomescape.exception.UnauthorizedException;

public class MemberNotFoundException extends UnauthorizedException {

    public MemberNotFoundException(String message) {
        super(message);
    }
}
