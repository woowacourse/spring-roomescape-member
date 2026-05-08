package roomescape.reservationtime.exception;

import lombok.Getter;

@Getter
public enum ReservationTimeErrorCode {
    RESERVATION_TIME_START_AT_NOT_NULL("예약 시간은 비어있을 수 없습니다."),
    RESERVATION_TIME_DUPLICATE("예약 시간은 중복 생성이 불가능합니다."),
    RESERVATION_TIME_NOT_FOUND("찾는 예약 시간이 없습니다.");

    private final String message;

    ReservationTimeErrorCode(String message) {
        this.message = message;
    }

}
