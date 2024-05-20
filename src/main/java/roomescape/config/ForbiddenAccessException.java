package roomescape.config;

import org.springframework.http.HttpStatus;
import roomescape.controller.exception.BaseException;

public class ForbiddenAccessException extends BaseException {

    public ForbiddenAccessException() {
        super("접근 권한이 없습니다.", "", HttpStatus.FORBIDDEN);
    }
}
