package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 예약을 찾을 수 없습니다."),
    RESERVATION_DUPLICATED(HttpStatus.CONFLICT, "동일한 예약이 이미 존재합니다."),
    RESERVATION_DATE_PASSED(HttpStatus.UNPROCESSABLE_ENTITY, "오늘보다 이전 날짜로 예약할 수 없습니다."),
    RESERVATION_TIME_PASSED(HttpStatus.UNPROCESSABLE_ENTITY, "현재 시각보다 이전 시간으로 예약할 수 없습니다."),

    RESERVATION_TIME_IN_USE(HttpStatus.CONFLICT, "해당 시간에 예약이 존재하여 삭제할 수 없습니다."),
    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 예약 시간을 찾을 수 없습니다."),

    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 테마를 찾을 수 없습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 에러가 발생했습니다.");

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
