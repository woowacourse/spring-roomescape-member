package roomescape.controller.reservation;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationRequest(String name, String date, Long timeId, Long themeId) {
    public Reservation toDomain(final ReservationTime time, final Theme theme) {
        return Reservation.from(null, name, date, time, theme);
    }
}
