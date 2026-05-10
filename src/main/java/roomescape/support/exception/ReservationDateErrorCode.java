package roomescape.support.exception;

import lombok.Getter;

@Getter
public enum ReservationDateErrorCode implements ErrorCode {

    RESERVATION_DATE_NOT_EXIST("존재하지 않는 날짜 입니다."),
    RESERVATION_DATE_IN_USE("이미 예약이 존재하는 날짜는 삭제할 수 없습니다."),
    ;

    private final String message;

    ReservationDateErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getCode() {
        return name();
    }
}
