package roomescape.global.auth;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.base.BusinessException;
import roomescape.global.exception.base.ErrorPolicy;

public class MissingAuthorizationHeaderException extends BusinessException {

    public MissingAuthorizationHeaderException() {
        super(new ErrorPolicy() {
            @Override
            public HttpStatus status() {
                return HttpStatus.UNAUTHORIZED;
            }

            @Override
            public String message() {
                return "Authorization 헤더가 필요합니다.";
            }
        });
    }
}
