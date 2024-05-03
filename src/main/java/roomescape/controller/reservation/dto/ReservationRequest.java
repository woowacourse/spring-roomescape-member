package roomescape.controller.reservation.dto;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationRequest(String name, String date, Long timeId, Long themeId) {
    public Reservation toDomain(final ReservationTime time, final Theme theme) {
        return new Reservation(null, name, date, time, theme);
    }
}
