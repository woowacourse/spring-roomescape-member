package roomescape.config;

import org.springframework.http.HttpStatus;
import roomescape.controller.exception.BaseException;

public class TokenValidationFailureException extends BaseException {

    public TokenValidationFailureException(String detail) {
        super("토큰이 검증이 실패했습니다.", detail, HttpStatus.UNAUTHORIZED);
    }
}
