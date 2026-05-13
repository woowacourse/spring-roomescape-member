package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다.", "예약 ID를 다시 확인해주세요."),
    RESERVATION_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 예약입니다.", "다른 테마, 날짜, 시간으로 예약을 시도해주세요."),
    RESERVATION_PAST_DATE(HttpStatus.UNPROCESSABLE_ENTITY, "지난 날짜는 예약할 수 없습니다.", "오늘 이후의 날짜로 예약해주세요."),
    RESERVATION_PAST_TIME(HttpStatus.UNPROCESSABLE_ENTITY, "지난 시간은 예약할 수 없습니다.", "현재 시각 이후의 시간으로 예약해주세요."),
    RESERVATION_ALREADY_STARTED(HttpStatus.UNPROCESSABLE_ENTITY, "이미 지난 예약은 변경하거나 취소할 수 없습니다.", "변경 가능한 예약을 다시 선택해주세요."),

    RESERVATION_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 시간을 찾을 수 없습니다.", "예약 시간 ID를 다시 확인해주세요."),
    RESERVATION_TIME_IN_USE(HttpStatus.CONFLICT, "예약이 존재하는 시간은 삭제할 수 없습니다.", "해당 시간의 예약을 먼저 취소해주세요."),

    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "테마를 찾을 수 없습니다.", "테마 ID를 다시 확인해주세요."),

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청 값이 유효하지 않습니다.", "요청 값을 확인 후 다시 시도해주세요."),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "요청 형식이 올바르지 않습니다.", "요청 값을 확인 후 다시 시도해주세요."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "요청 처리에 문제가 발생했습니다.", null);

    private final HttpStatus status;
    private final String message;
    private final String action;

    ErrorCode(HttpStatus status, String message, String action) {
        this.status = status;
        this.message = message;
        this.action = action;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getAction() {
        return action;
    }
}
