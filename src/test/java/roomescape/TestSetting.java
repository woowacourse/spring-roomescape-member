package roomescape;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationTimeRequest;

public class TestSetting {

    private TestSetting() {
    }

    public static Reservation createReservation() {
        String name = "ted";
        LocalDate date = LocalDate.parse("2024-01-01");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("10:00"));
        return new Reservation(name, date, reservationTime);
    }

    public static ReservationTime createReservationTime() {
        return new ReservationTime(LocalTime.parse("10:00"));
    }

    public static ReservationRequest createReservationRequest() {
        return new ReservationRequest(
                "ted",
                LocalDate.parse("2024-01-01"),
                1L);
    }

    public static ReservationTimeRequest createReservationTimeRequest() {
        return new ReservationTimeRequest(LocalTime.parse("10:00"));
    }

    public static boolean isEqualsReservation(Reservation reservation1, Reservation reservation2) {
        if (reservation1 == reservation2) {
            return true;
        }
        if (reservation1 == null || reservation2 == null) {
            return false;
        }

        return Objects.equals(reservation1.getName(), reservation2.getName())
                && Objects.equals(reservation1.getDate(), reservation2.getDate())
                && isEqualsReservationTime(reservation1.getTime(), reservation2.getTime()
        );
    }

    public static boolean isEqualsReservationTime(ReservationTime reservationTime1, ReservationTime reservationTime2) {
        if (reservationTime1 == reservationTime2) {
            return true;
        }
        if (reservationTime1 == null || reservationTime2 == null) {
            return false;
        }

        return Objects.equals(reservationTime1.getStartAt(), reservationTime2.getStartAt());
    }
}
