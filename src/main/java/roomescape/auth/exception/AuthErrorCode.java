package roomescape.auth.exception;

import roomescape.global.exception.ErrorCode;

public enum AuthErrorCode implements ErrorCode {
    LOGIN_FAILED("A001", "로그인에 실패하였습니다."),
    COOKIE_NULL("A002", "쿠키가 비어있습니다."),
    COOKIE_NOT_FOUND("A003", "필요한 쿠키가 존재하지 않습니다."),
    UNAUTHORIZED_ACCESS("A004", "접근 권한이 없습니다."),
    TOKEN_IS_EMPTY("A005", "토큰이 비어있습니다."),
    TOKEN_IS_EXPIRED("A006", "토큰이 만료되었습니다."),
    TOKEN_IS_INVALID("A007", "유효하지 않은 토큰입니다."),
    TOKEN_PARSE_FAILED("A008", "토큰 파싱에 실패했습니다.");;

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
