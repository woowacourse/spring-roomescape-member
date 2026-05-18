package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."),
    PAST_DATE_RESERVATION(HttpStatus.UNPROCESSABLE_ENTITY, "과거 날짜로는 예약할 수 없습니다."),
    RESERVATION_ACCESS_DENIED(HttpStatus.FORBIDDEN, "타인의 예약은 수정/삭제할 수 없습니다."),

    TIME_NOT_FOUND(HttpStatus.NOT_FOUND,  "존재하지 않는 시간입니다."),
    TIME_IN_USE(HttpStatus.UNPROCESSABLE_ENTITY, "예약이 존재하는 시간은 삭제할 수 없습니다."),
    RESERVATION_TIME_CONFLICT(HttpStatus.UNPROCESSABLE_ENTITY, "이미 예약된 시간대입니다."),

    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 테마입니다."),

    MISSING_CREDENTIALS(HttpStatus.UNAUTHORIZED, "삭제를 위한 권한(role) 또는 식별자(name) 정보가 필요합니다."),
    ADMIN_ROLE_REQUIRED(HttpStatus.FORBIDDEN, "관리자 권한이 필요한 요청입니다."),

    BLANK_INPUT(HttpStatus.BAD_REQUEST, "필수 입력 값이 누락되었습니다."),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "올바르지 않은 형식입니다."),
    INVALID_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 값입니다."),

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

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