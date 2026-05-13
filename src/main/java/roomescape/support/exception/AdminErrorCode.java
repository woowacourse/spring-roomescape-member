package roomescape.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AdminErrorCode implements ErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,
        "권한이 없는 사용자입니다.", "관리자 토큰을 다시 입력해주세요."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String action;

    AdminErrorCode(HttpStatus httpStatus, String message, String action) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.action = action;
    }

    @Override
    public String getCode() {
        return name();
    }
}
