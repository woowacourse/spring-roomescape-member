package roomescape.reservation.exception;

import lombok.Getter;

@Getter
public enum ReservationErrorCode {
    RESERVATION_NAME_NOT_BLANK("예약자 이름은 비어있을 수 없습니다."),
    RESERVATION_DATE_NOT_NULL("예약 날짜는 비어있을 수 없습니다."),
    RESERVATION_INVALID_DATE("예약 날짜는 미래여야 합니다."),
    RESERVATION_TIME_NOT_NULL("예약 시간 정보가 없습니다."),
    RESERVATION_DUPLICATE("예약은 중복 생성이 불가능합니다."),
    RESERVATION_NOT_FOUND("찾는 예약이 없습니다.");

    private final String message;

    ReservationErrorCode(String message) {
        this.message = message;
    }

}
