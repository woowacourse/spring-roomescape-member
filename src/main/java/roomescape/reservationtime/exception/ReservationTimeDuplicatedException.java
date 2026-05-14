package roomescape.reservationtime.exception;

import java.time.LocalTime;

public class ReservationTimeDuplicatedException extends RuntimeException {

    public static final String MESSAGE = "이미 등록된 예약 시간입니다.";

    public ReservationTimeDuplicatedException(LocalTime startAt) {
        super(MESSAGE + " startAt=" + startAt);
    }

}
