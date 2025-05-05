package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationException extends CustomException {
    public AuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
