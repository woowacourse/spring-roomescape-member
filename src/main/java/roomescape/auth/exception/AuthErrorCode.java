package roomescape.auth.exception;

import roomescape.global.exception.ErrorCode;

public enum AuthErrorCode implements ErrorCode {
    LOGIN_FAILED("A001", "로그인에 실패하였습니다."),
    COOKIE_NULL("A002", "쿠키가 비어있습니다."),
    COOKIE_NOT_FOUND("A003", "필요한 쿠키가 존재하지 않습니다."),
    UNAUTHORIZED_ACCESS("A004", "접근 권한이 없습니다.");

    private final String code;
    private final String message;

    AuthErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
