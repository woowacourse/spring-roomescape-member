package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 부족합니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 자원을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 자원입니다."),

    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    INVALID_DATETIME_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 날짜/시간 형식입니다."),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."),
    INVALID_MEMBER_INFO(HttpStatus.UNAUTHORIZED, "가입되지 않은 이메일이거나, 비밀번호가 잘못되었습니다."),
    CONFLICT_RESERVATION_TIME(HttpStatus.CONFLICT, "예약이 존재하는 시간은 삭제할 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public int getStatusValue() {
        return status.value();
    }

    public String getMessage() {
        return message;
    }
}
