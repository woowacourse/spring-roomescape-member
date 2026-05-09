package roomescape.support.exception;

import lombok.Getter;

@Getter
public enum ThemeErrorCode implements ErrorCode {

    INVALID_THEME("테마는 필수입니다."),
    THEME_NOT_EXIST("존재하지 않는 테마 입니다."),
    THEME_IN_USE("이미 예약이 존재하는 테마는 삭제할 수 없습니다."),
    ;

    private final String message;

    ThemeErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getCode() {
        return name();
    }
}
