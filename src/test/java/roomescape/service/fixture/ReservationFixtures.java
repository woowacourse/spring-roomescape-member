package roomescape.service.fixture;

import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationName;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.dto.reservation.ReservationCreateRequest;

public class ReservationFixtures {

    public static Reservation createReservation(String name, String date, ReservationTime time, Theme theme) {
        return new Reservation(null, new ReservationName(name), ReservationDate.from(date), time, theme);
    }

    public static Reservation createReservation(ReservationTime time, Theme theme) {
        return new Reservation(null, new ReservationName("default"), ReservationDate.from("2024-09-02"), time, theme);
    }

    public static Reservation createReservation(String date, ReservationTime time, Theme theme) {
        return new Reservation(null, new ReservationName("default"), ReservationDate.from(date), time, theme);
    }

    public static ReservationCreateRequest createReservationCreateRequest(String name, String date, long timeId, long themeId) {
        return ReservationCreateRequest.of(name, date, timeId, themeId);
    }

    public static ReservationCreateRequest createReservationCreateRequest(long timeId, long themeId) {
        return ReservationCreateRequest.of("default", "2024-04-02", timeId, themeId);
    }
}
