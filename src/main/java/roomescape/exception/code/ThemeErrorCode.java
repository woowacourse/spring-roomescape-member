package roomescape.exception.code;

import org.springframework.http.HttpStatus;
import roomescape.exception.ErrorCode;

public enum ThemeErrorCode implements ErrorCode {

    THEME_ALREADY_EXISTS("T-409-001", HttpStatus.CONFLICT, "이미 존재하는 테마입니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    ThemeErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
