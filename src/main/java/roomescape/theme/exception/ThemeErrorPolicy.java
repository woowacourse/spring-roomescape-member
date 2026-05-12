package roomescape.theme.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.base.ErrorPolicy;

public enum ThemeErrorPolicy implements ErrorPolicy {
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "테마가 존재하지 않습니다."),
    DUPLICATE_THEME(HttpStatus.CONFLICT, "테마가 이미 존재합니다."),
    THEME_IN_USE(HttpStatus.CONFLICT, "해당 테마의 예약이 존재합니다."),
    INVALID_THEME_REQUEST(HttpStatus.BAD_REQUEST, "테마 등록 요청 값이 유효하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ThemeErrorPolicy(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus status() {
        return status;
    }

    @Override
    public String message() {
        return message;
    }
}
