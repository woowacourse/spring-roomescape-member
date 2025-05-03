package roomescape.domain;

import java.time.Clock;
import java.time.LocalDateTime;
import roomescape.domain.exception.PastReservationException;

public class ReservationDateTime {

    private final ReservationDate reservationDate;
    private final ReservationTime reservationTime;

    public ReservationDateTime(ReservationDate reservationDate, ReservationTime reservationTime, Clock clock) {
        validatePast(reservationDate, reservationTime, clock);
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
    }

    private void validatePast(ReservationDate reservationDate, ReservationTime reservationTime, Clock clock) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate.getDate(), reservationTime.getStartAt());
        LocalDateTime now = LocalDateTime.now(clock);

        if (reservationDateTime.isBefore(now)) {
            throw new PastReservationException("[ERROR] 현재 시간 이후로 예약할 수 있습니다.");
        }
    }

    public ReservationDate getReservationDate() {
        return reservationDate;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }
}
