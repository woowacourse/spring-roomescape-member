package roomescape.reservation.exception;

import java.time.LocalDate;

public class ReservationDuplicatedException extends RuntimeException {

    public ReservationDuplicatedException(LocalDate date, Long reservationTimeId, Long themeId) {
        super(String.format("이미 예약이 있습니다. date=%s, reservationTimeId=%d, themeId=%d",
                date, reservationTimeId, themeId));
    }

}
