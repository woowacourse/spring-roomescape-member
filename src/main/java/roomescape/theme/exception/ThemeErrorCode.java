package roomescape.theme.exception;

import roomescape.global.ErrorCode;

public enum ThemeErrorCode implements ErrorCode {
    THEME_NOT_FOUND("THEME_001", "테마가 존재하지 않습니다."),

    ;

    private final String code;
    private final String message;

    ThemeErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
