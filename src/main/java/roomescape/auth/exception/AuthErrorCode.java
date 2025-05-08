package roomescape.auth.exception;

import roomescape.global.exception.ErrorCode;

public enum AuthErrorCode implements ErrorCode {
    LOGIN_FAILED("A001", "로그인에 실패하였습니다.");

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
