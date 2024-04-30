package roomescape.fixture;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.time.ReservationTime;

public class ReservationFixture {
    public static Reservation reservation() {
        return reservation(1L);
    }

    public static Reservation reservation(long id) {
        return new Reservation(id, "prin", LocalDate.of(2024, 4, 23), reservationTime());
    }

    public static Reservation reservation(String name, String date, ReservationTime time) {
        return new Reservation(name, LocalDate.parse(date), time);
    }

    public static ReservationTime reservationTime() {
        return reservationTime(1L);
    }

    public static ReservationTime reservationTime(long id) {
        return new ReservationTime(id, LocalTime.of(22, 11));
    }

    public static ReservationTime reservationTime(String startAt) {
        return new ReservationTime(LocalTime.parse(startAt));
    }
}
