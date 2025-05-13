package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // User
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "[ERROR] 토큰이 유효하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "[ERROR] 존재하지 않는 사용자입니다."),
    ROLE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "[ERROR] 존재하지 않는 role입니다."),
    INVALID_ROLE(HttpStatus.FORBIDDEN, "[ERROR] 해당 페이지의 권한이 없습니다."),

    // Time
    TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "[ERROR] 존재하지 않는 시간대입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
