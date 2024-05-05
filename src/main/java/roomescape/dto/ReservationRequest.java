package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {

    public Reservation toReservation(ReservationTime time, Theme theme) {
        return new Reservation(name, date, time, theme);
    }
}
