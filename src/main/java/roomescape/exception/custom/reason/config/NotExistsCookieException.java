package roomescape.exception.custom.reason.config;

import roomescape.exception.custom.status.BadRequestException;

public class NotExistsCookieException extends BadRequestException {
    public NotExistsCookieException() {
        super("쿠키가 존재하지 않습니다.");
    }
}
