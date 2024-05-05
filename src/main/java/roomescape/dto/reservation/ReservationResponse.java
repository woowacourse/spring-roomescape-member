package roomescape.dto.reservation;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;

public record ReservationResponse(Long id, String name, String date, ReservationTime time, ReservationTheme theme) {

    public ReservationResponse(Reservation reservation) {
        this(reservation.getId(), reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }
}
