package roomescape.reservation.domain.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public record ReservationRequestDto(String name, LocalDate date, Long timeId, Long themeId) {

    public Reservation toEntity(ReservationTime reservationTime, Theme theme) {
        return Reservation.of(name, date, reservationTime, theme);
    }
}
