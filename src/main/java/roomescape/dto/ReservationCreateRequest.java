package roomescape.dto;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationCreateRequest(String name, String date, Long timeId, Long themeId) {
    public Reservation createReservation(ReservationTime time, Theme theme) {
        return new Reservation(name, date, time, theme);
    }
}
