package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.ErrorCode;

public enum AuthErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND("해당 email의 member가 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    LOGIN_REQUIRED("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN_ACCESS("접근 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String message;
    private final HttpStatus status;

    AuthErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
