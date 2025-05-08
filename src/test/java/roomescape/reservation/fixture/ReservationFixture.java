package roomescape.reservation.fixture;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationRequestDto;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public class ReservationFixture {

    public static Reservation create(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        return Reservation.of(name, date, reservationTime, theme);
    }

    public static ReservationRequestDto createRequestDto(String name, LocalDate date, Long timeId, Long themeId) {
        return new ReservationRequestDto(name, date, timeId, themeId);
    }
}
