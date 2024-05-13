package roomescape.fixture;

import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.dto.reservation.ReservationCreateRequest;

public class ReservationFixtures {

    private ReservationFixtures() {
    }

    public static Reservation createReservation(Member member, String date, ReservationTime time, Theme theme) {
        return new Reservation(null, member, ReservationDate.from(date), time, theme);
    }

    public static Reservation createReservation(Member member, ReservationTime time, Theme theme) {
        return new Reservation(null, member, ReservationDate.from("2024-09-02"), time, theme);
    }

    public static ReservationCreateRequest getReservationCreateRequest(String date, long timeId, long themeId) {
        return ReservationCreateRequest.of(date, timeId, themeId);
    }

    public static ReservationCreateRequest getReservationCreateRequest(long timeId, long themeId) {
        return ReservationCreateRequest.of("2024-04-02", timeId, themeId);
    }
}
