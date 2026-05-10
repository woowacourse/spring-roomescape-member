package roomescape.support.exception;

import lombok.Getter;

@Getter
public enum ReservationErrorCode implements ErrorCode {

    INVALID_RESERVATION_NAME("이름은 비어 있을 수 없습니다."),
    INVALID_RESERVATION_DATE("날짜는 필수입니다."),
    RESERVATION_NOT_FOUND("존재하지 않는 예약건 입니다"),
    ;

    private final String message;

    ReservationErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getCode() {
        return name();
    }
}
