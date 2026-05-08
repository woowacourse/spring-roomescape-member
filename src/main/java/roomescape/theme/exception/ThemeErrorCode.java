package roomescape.theme.exception;

import lombok.Getter;

@Getter
public enum ThemeErrorCode {
    THEME_NAME_NOT_BLANK("테마의 이름은 비어있울 수 없습니다."),
    THEME_DUPLICATE("테마는 중복 생성이 불가능합니다."),
    THEME_NOT_FOUND("찾는 테마가 없습니다.");

    private final String message;

    ThemeErrorCode(String message) {
        this.message = message;
    }

}
