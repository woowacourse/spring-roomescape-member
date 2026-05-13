package roomescape.exception;

public enum ErrorMessage {

    CANNOT_SELECT_PAST_DATETIME("지나간 날짜, 시간에 대한 예약 생성은 불가능합니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
