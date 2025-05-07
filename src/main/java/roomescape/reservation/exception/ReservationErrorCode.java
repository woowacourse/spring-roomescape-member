package roomescape.reservation.exception;

import roomescape.global.exception.ErrorCode;

public enum ReservationErrorCode implements ErrorCode {
    RESERVATION_ALREADY_EXISTS("R001", "이미 예약이 존재합니다."),
    RESERVATION_FIELD_REQUIRED("R002", "필수 입력값이 누락되었습니다."),
    RESERVATION_IN_PAST("R003", "예약 가능한 시간이 지났습니다."),
    RESERVATION_NOT_FOUND("R004", "예약을 찾을 수 없습니다.");

    private final String code;
    private final String message;

    ReservationErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
