package roomescape.domain.theme.exception;

import org.springframework.http.HttpStatus;
import roomescape.common.exception.ErrorCode;

public enum ThemeErrorCode implements ErrorCode {

    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "테마를 찾을 수 없습니다."),
    THEME_DELETE_CONFLICT(HttpStatus.CONFLICT, "예약이 존재하는 테마는 삭제할 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ThemeErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
