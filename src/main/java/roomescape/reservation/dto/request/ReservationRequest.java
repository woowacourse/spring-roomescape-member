package roomescape.reservation.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public record ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {

    public Reservation toEntity(ReservationTime reservationTime, Theme theme) {
        return new Reservation(null, name, date, reservationTime, theme, LocalDateTime.now());
    }
}
