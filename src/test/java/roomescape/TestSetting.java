package roomescape;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationTimeRequest;

public class TestSetting {

    private TestSetting() {
    }

    public static Reservation createReservation(ReservationTime reservationTime, Theme theme) {
        String name = "ted";
        LocalDate date = LocalDate.parse("2024-01-01");
        return new Reservation(name, date, reservationTime, theme);
    }

    public static ReservationTime createReservationTime() {
        return new ReservationTime(LocalTime.parse("10:00"));
    }

    public static Theme createTheme() {
        String name = "테마명";
        String description = "테마 설명 테마 설명 테마 설명";
        String thumbnail = "썸네일명.jpg";
        return new Theme(name, description, thumbnail);
    }

    public static ReservationRequest createReservationRequest() {
        return new ReservationRequest(
                "ted",
                LocalDate.parse("2024-01-01"),
                1L,
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
