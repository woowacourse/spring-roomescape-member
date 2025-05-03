package roomescape.domain;

import java.time.Clock;
import java.time.LocalDateTime;
import roomescape.domain.exception.PastReservationException;

public record ReservationDateTime(ReservationDate reservationDate, ReservationTime reservationTime) {

    public static ReservationDateTime create(ReservationDate reservationDate, ReservationTime reservationTime, Clock clock) {
        validatePast(reservationDate, reservationTime, clock);
        return new ReservationDateTime(reservationDate, reservationTime);
    }

    private static void validatePast(ReservationDate reservationDate, ReservationTime reservationTime, Clock clock) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate.getDate(), reservationTime.getStartAt());
        LocalDateTime now = LocalDateTime.now(clock);

        if (reservationDateTime.isBefore(now)) {
            throw new PastReservationException("[ERROR] 현재 시간 이후로 예약할 수 있습니다.");
        }
    }
}
