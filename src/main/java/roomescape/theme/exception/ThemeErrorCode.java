package roomescape.theme.exception;

import roomescape.global.exception.ErrorCode;

public enum ThemeErrorCode implements ErrorCode {
    THEME_FIELD_REQUIRED("TH001", "필수 입력값이 누락되었습니다."),
    THEME_NOT_FOUND("TH002", "테마를 찾을 수 없습니다.");

    private final String code;
    private final String message;

    ThemeErrorCode(String code, String message) {
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
