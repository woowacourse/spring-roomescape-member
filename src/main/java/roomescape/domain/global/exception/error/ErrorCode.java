package roomescape.domain.global.exception.error;

public enum ErrorCode {

    COMMON_INVALID_REQUEST("요청 형식이 잘못되었습니다."),
    COMMON_INVALID_PARAMETER_TYPE("요청 파라미터의 형식이 잘못되었습니다."),
    COMMON_INVALID_REQUEST_BODY("요청 본문의 형식이 잘못되었습니다."),

    RESERVATION_FORBIDDEN("요청한 예약에 접근할 권한이 없습니다."),
    RESERVATION_NOT_FOUND("요청한 예약을 찾을 수 없습니다."),
    RESERVATION_DUPLICATE("이미 존재하는 예약입니다."),
    RESERVATION_ALREADY_PASSED("지난 예약은 접근할 수 없습니다."),
    RESERVATION_TIME_ALREADY_PASSED("지난 시점으로는 변경할 수 없습니다."),

    TIME_NOT_FOUND("요청한 시간을 찾을 수 없습니다."),
    TIME_DUPLICATE("이미 존재하는 시간입니다."),
    TIME_REFERENCED_BY_RESERVATION("요청한 시간을 참조하는 예약이 존재합니다."),
    TIME_INVALID_DATE("지난 날짜로 시간 조회를 시도했습니다."),

    THEME_NOT_FOUND("요청한 테마를 찾을 수 없습니다."),
    THEME_DUPLICATE("이미 존재하는 테마입니다."),
    THEME_REFERENCED_BY_RESERVATION("요청한 테마를 참조하는 예약이 존재합니다."),
    THEME_INVALID_DATE("시작 일자는 종료 일자 이전 시점이어야 합니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
