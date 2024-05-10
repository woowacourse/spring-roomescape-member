package roomescape.exception;

public enum ErrorType {
    INVALID_DATA_TYPE("요청 데이터 형태가 일치하지 않습니다."),
    INVALID_DATA_VALUE("허용하지 않는 데이터값 입니다."),
    EMPTY_VALUE_NOT_ALLOWED("필수로 입력해야하는 값이 누락되어 있습니다."),
    INVALID_TIME("올바르지 않는 시간 값 입니다."),
    INVALID_THEME("올바르지 않는 테마 값 입니다."),
    PAST_DATE_NOT_ALLOWED("이미 지난 시간으로 예약할 수 없습니다."),
    NOT_EXIST_TIME("존재하지 않는 예약 시간입니다."),
    NOT_EXIST_EMAIL("회원 정보가 존재하지 않습니다.");

    private final String message;

    ErrorType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
