package roomescape.global.exception.error;

public enum ErrorType {

    // 400 Bad Request
    INVALID_ERROR("유효하지 않은 값입니다."),
    BAD_REQUEST("요청 형식이 잘못되었습니다."),

    // 409 Conflict
    TIME_IS_USED_CONFLICT("삭제할 수 없는 시간대입니다."),
    TIME_DUPLICATION_CONFLICT("이미 해당 시간이 존재합니다."),
    RESERVATION_DUPLICATION_CONFLICT("이미 예약이 존재합니다."),
    RESERVATION_PERIOD_CONFLICT("이미 지난 시간대는 예약할 수 없습니다."),
    
    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR("서버 내부에서 에러가 발생하였습니다."),
    ;


    private final String description;

    ErrorType(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
