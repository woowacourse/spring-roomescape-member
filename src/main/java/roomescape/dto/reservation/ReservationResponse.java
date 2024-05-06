package roomescape.dto.reservation;

import roomescape.domain.Reservation;
import roomescape.dto.theme.ThemeResponse;
import roomescape.dto.time.TimeResponse;

public record ReservationResponse(Long id, String name, String date, TimeResponse time, ThemeResponse theme) {

    public ReservationResponse(Reservation reservation) {
        this(reservation.getId(), reservation.getName(), reservation.getDate(), new TimeResponse(reservation.getTime()),
                new ThemeResponse(reservation.getTheme()));
    }
}
