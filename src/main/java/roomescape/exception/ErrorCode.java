package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // Reservation
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다. 예약 정보를 다시 확인해주세요."),
    RESERVATION_NOT_OWNER(HttpStatus.FORBIDDEN, "해당 예약에 대한 권한이 없습니다. 로그인 정보를 확인해주세요."),
    RESERVATION_PAST_TIME(HttpStatus.BAD_REQUEST, "이미 지나간 시간에는 예약할 수 없습니다. 현재 이후의 시간을 선택해주세요."),
    RESERVATION_ALREADY_PASSED(HttpStatus.BAD_REQUEST, "이미 종료되거나 지나간 예약은 처리할 수 없습니다. 선택하신 예약 시간을 다시 확인해 주세요."),
    RESERVATION_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "예약 변경에 실패했습니다. 본인의 예약이 맞는지, 또는 취소된 예약인지 다시 확인해 주세요."),
    ALREADY_RESERVED_SCHEDULE(HttpStatus.CONFLICT, "이미 예약된 스케줄입니다. 다른 시간을 선택해주세요."),
    // Schedule
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 스케줄입니다. 날짜와 테마를 다시 확인해주세요."),
    SCHEDULE_IN_USE(HttpStatus.BAD_REQUEST, "해당 스케줄에 예약이 존재하여 삭제할 수 없습니다. 예약을 먼저 처리해주세요."),
    INVALID_SCHEDULE_TIME(HttpStatus.BAD_REQUEST, "스케줄 시간이 운영 시간을 벗어납니다. 운영 시간을 확인 후 다시 시도해주세요."),
    PAST_SCHEDULE_CREATION(HttpStatus.BAD_REQUEST, "과거 날짜나 시간으로는 스케줄을 생성할 수 없습니다. 현재 이후의 시간을 선택해주세요."),
    DUPLICATE_SCHEDULE_TIME(HttpStatus.CONFLICT, "해당 시간에 이미 다른 스케줄이 존재합니다. 다른 시간을 선택해주세요."),
    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "가입되지 않은 유저입니다. 로그인을 다시 확인해주세요."),
    // Theme
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 테마입니다. 목록에서 테마를 다시 선택해주세요.");

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
