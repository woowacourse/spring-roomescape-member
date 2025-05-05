package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.ErrorCode;

public enum AuthErrorCode implements ErrorCode {
    USER_NOT_FOUND("해당 email의 user가 존재하지 않습니다."),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN("유효하지 않은 Token입니다."),
    LOGIN_REQUIRED("로그인이 필요합니다."),
    ;

    private final String message;

    AuthErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
