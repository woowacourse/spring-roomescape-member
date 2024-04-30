package roomescape.reservation.handler.exception;

public enum ExceptionCode {

    INVALID_NAME_LENGTH("이름은 1자 이상 10자 이하로 입력해야 합니다."),
    NOT_FOUND_RESERVATION_TIME("예약 시간을 찾을 수 없습니다."),
    TIME_IN_USE("이미 해당 시간에 예약이 존재하여 삭제할 수 없습니다."),
    PAST_TIME_SLOT_RESERVATION("이미 지나간 시점을 예약할 수 없습니다."),
    DUPLICATE_RESERVATION("동일한 시간에 중복 예약을 할 수 없습니다."),
    ;

    private final String errorMessage;

    ExceptionCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
