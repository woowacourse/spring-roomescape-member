package roomescape.exception;

public enum ErrorCode {
    RESERVATION_BLANK_NAME(400, "name이 누락되었습니다.", "requestBody에 name 값을 추가해주세요."),
    RESERVATION_BLANK_DATE(400, "date가 누락되었습니다.", "requestBody에 date 값을 추가해주세요."),
    RESERVATION_BLANK_TIMEID(400, "timeId가 누락되었습니다.", "requestBody에 timeId 값을 추가해주세요."),
    RESERVATION_BLANK_THEMEID(400, "themeId가 누락되었습니다.", "requestBody에 themeId 값을 추가해주세요."),
    RESERVATION_WRONG_NAME(400, "이름은 2자 이상 20자 이하 입니다.", null),
    RESERVATION_INVALID_DATE(400, "날짜 형식이 잘못되었습니다.", "날짜 형식은 2026-05-11 형태여야 합니다."),
    RESERVATION_WRONG_DATE(400, "이미 지난 날짜로는 예약이 불가능합니다.", null),
    RESERVATION_WRONG_TIME(400, "이미 지난 시간으로는 예약이 불가능합니다.", null),
    RESERVATION_TIMEID_NOT_FOUND(404, "해당 시간은 존재하지 않습니다.", null),
    RESERVATION_THEMEID_NOT_FOUND(404, "해당 테마는 존재하지 않습니다.", null),
    RESERVATION_DUPLICATE(409, "중복된 예약이 존재합니다.", "다른 날짜 혹은 시간, 테마로 예약해 주세요."),

    RESERVATION_NOT_FOUND(404, "해당 예약이 존재하지 않아서 삭제할 수 없습니다.", "예약 아이디를 확인해주세요."),

    TIME_BLANK_STARTAT(400, "startAt이 누락되었습니다.", "requestBody에 startAt 값을 추가해주세요."),
    TIME_WRONG_STARTAT(400, "startAt의 값이 잘못되었습니다.", "TIME의 형태에 맞게 작성해주세요. ex) 08:00"),
    TIME_DUPLICATE(409, "이미 존재하는 시간이 있습니다.", "다른 startAt을 입력해주세요."),

    TIME_NOT_FOUND(404, "해당 시간이 존재하지 않아서 삭제할 수 없습니다.", "시간 아이디를 확인해주세요."),
    TIME_CANNOT_DELETE(409, "해당 시간의 예약이 존재하기에 삭제할 수 없습니다.", "해당 시간의 모든 예약을 삭제한 후, 재시도 해주세요."),

    THEME_BLANK_NAME(400, "name이 누락되었습니다.", "requestBody에 name 값을 추가해주세요."),
    THEME_WRONG_NAME(400, "이름은 2자 이상 20자 이하 입니다.", null),
    THEME_BLANK_URL(400, "url이 누락되었습니다.", "requestBody에 url 값을 추가해주세요."),

    THEME_NOT_FOUND(404, "해당 테마가 존재하지 않아서 삭제할 수 없습니다.", "테마 아이디를 확인해주세요."),

    THEME_RANK_BLANK_LIMIT(400, "limit 값이 누락되었습니다.", "queryString에 limit값을 추가해주세요."),
    THEME_RANK_INVALID_LIMIT(400, "limit는 0 이하일 수 없습니다.", null),

    INTERNAL_ERROR(500, "요청 처리에 문제가 발생했습니다.", null);

    private final int status;
    private final String message;
    private final String action;

    ErrorCode(int status, String message, String action) {
        this.status = status;
        this.message = message;
        this.action = action;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getAction() {
        return action;
    }
}
