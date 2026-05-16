package roomescape.exception;

public enum ErrorCode {
    RESERVATION_WRONG_NAME(400, "이름은 2자 이상 20자 이하 입니다."),
    RESERVATION_WRONG_DATE(400, "이미 지난 날짜로는 예약이 불가능합니다."),
    RESERVATION_WRONG_TIME(400, "이미 지난 시간으로는 예약이 불가능합니다."),
    RESERVATION_TIMEID_NOT_FOUND(404, "해당 시간은 존재하지 않습니다."),
    RESERVATION_THEMEID_NOT_FOUND(404, "해당 테마는 존재하지 않습니다."),
    RESERVATION_DUPLICATE(409, "중복된 예약이 존재합니다. 다른 날짜, 시간, 테마로 예약해 주세요."),
    RESERVATION_NOT_FOUND(404, "해당 예약이 존재하지 않아서 삭제할 수 없습니다. 예약 아이디를 확인해주세요."),
    TIME_WRONG_STARTAT(400, "startAt의 값이 잘못되었습니다. TIME 형태에 맞게 작성해주세요. ex) 08:00"),
    TIME_DUPLICATE(409, "이미 존재하는 시간이 있습니다. 다른 startAt을 입력해주세요."),
    TIME_NOT_FOUND(404, "해당 시간이 존재하지 않아서 삭제할 수 없습니다. 시간 아이디를 확인해주세요."),
    TIME_CANNOT_DELETE(409, "해당 시간의 예약이 존재하기에 삭제할 수 없습니다. 해당 시간의 모든 예약을 삭제한 후 재시도 해주세요."),
    THEME_WRONG_NAME(400, "이름은 2자 이상 20자 이하 입니다."),
    THEME_NOT_FOUND(404, "해당 테마가 존재하지 않아서 삭제할 수 없습니다. 테마 아이디를 확인해주세요."),
    INTERNAL_ERROR(500, "요청 처리에 문제가 발생했습니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
