package roomescape.reservation.exception;

import java.time.LocalDate;

public class ReservationDuplicatedException extends RuntimeException {

    public static final String MESSAGE = "이미 예약이 있습니다.";

    public ReservationDuplicatedException(LocalDate date, Long reservationTimeId, Long themeId) {
        super(String.format("%s date=%s, reservationTimeId=%d, themeId=%d", MESSAGE,
                date, reservationTimeId, themeId));
    }

}
