package roomescape.reservation.exception;

import roomescape.global.ErrorCode;

public enum ReservationErrorCode implements ErrorCode {

    RESERVATION_NOT_FOUND("RESERVATION_001", "예약이 존재하지 않습니다."),
    DUPLICATE_RESERVATION("RESERVATION_002", "예약이 이미 존재합니다."),
    INVALID_RESERVATION_DATE("RESERVATION_003", "예약 날짜가 유효하지 않습니다."),
    INVALID_RESERVATION_REQUEST("RESERVATION_004", "예약 요청 값이 올바르지 않습니다.");

    private final String code;
    private final String message;

    ReservationErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
