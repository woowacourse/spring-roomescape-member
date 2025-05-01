package roomescape.reservation.domain;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.reservation.domain.exception.PastReservationException;
import roomescape.reservation.domain.exception.ReservationDateNullException;
import roomescape.reservation.domain.exception.ReservationTimeNullException;
import roomescape.time.domain.ReservationTime;

public class ReservationDateTime {

    private final ReservationDate reservationDate;
    private final ReservationTime reservationTime;

    public ReservationDateTime(ReservationDate reservationDate, ReservationTime reservationTime, Clock clock) {
        validatePast(reservationDate, reservationTime, clock);
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
    }

    private void validatePast(ReservationDate reservationDate, ReservationTime reservationTime, Clock clock) {
        LocalDate localDate = reservationDate.getDate();
        LocalTime localTime = reservationTime.getStartAt();

        LocalDateTime reservationDateTime = LocalDateTime.of(localDate, localTime);
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
