package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // User
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "[ERROR] 유효하지 않은 토큰입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "[ERROR] 존재하지 않는 사용자입니다.");

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
