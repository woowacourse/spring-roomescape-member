package roomescape.theme.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.ErrorPolicy;

public enum ThemeErrorPolicy implements ErrorPolicy {
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "테마가 존재하지 않습니다."),

    ;

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
