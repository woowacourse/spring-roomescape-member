package roomescape.reservation.fixture;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationReqDto;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public class ReservationFixture {

    public static Reservation create(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        return Reservation.of(name, date, reservationTime, theme);
    }

    public static ReservationReqDto createReqDto(String name, LocalDate date, Long timeId, Long themeId) {
        return new ReservationReqDto(name, date, timeId, themeId);
    }
}
