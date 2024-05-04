package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationAddRequest(String name, LocalDate date, long timeId, long themeId) {
    public Reservation toEntity(ReservationTime reservationTime, Theme theme) {
        return new Reservation(null, name, date, reservationTime, theme);
    }
}
