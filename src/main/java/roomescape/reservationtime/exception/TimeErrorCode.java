package roomescape.reservationtime.exception;

import roomescape.global.exception.ErrorCode;

public enum TimeErrorCode implements ErrorCode {
    TIME_FIELD_REQUIRED("RT001", "필수 입력값이 누락되었습니다."),
    TIME_ALREADY_EXISTS("RT002", "이미 존재하는 예약 시간입니다."),
    TIME_NOT_FOUNT("RT003", "예약 시간을 찾을 수 없습니다."),
    USING_TIME("R004", "예약 되어있는 시간입니다.");

    private final String code;
    private final String message;

    TimeErrorCode(String code, String message) {
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
