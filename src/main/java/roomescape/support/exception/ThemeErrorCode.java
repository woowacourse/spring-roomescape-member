package roomescape.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ThemeErrorCode implements ErrorCode {
    INVALID_THEME(HttpStatus.BAD_REQUEST, "테마는 필수입니다."),
    THEME_NOT_EXIST(HttpStatus.NOT_FOUND, "존재하지 않는 테마 입니다."),
    THEME_IN_USE(HttpStatus.CONFLICT, "이미 예약이 존재하는 테마는 삭제할 수 없습니다."),
    INVALID_THEME_NAME(HttpStatus.BAD_REQUEST, "테마 제목은 비어 있을 수 없습니다."),
    INVALID_THEME_CONTENT(HttpStatus.BAD_REQUEST, "테마 설명은 비어 있을 수 없습니다."),
    INVALID_THEME_URL(HttpStatus.BAD_REQUEST, "테마 포스터 url은 비어 있을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    ThemeErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return name();
    }
}
