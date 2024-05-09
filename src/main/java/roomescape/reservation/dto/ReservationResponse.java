package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;

public record ReservationResponse(Long id, String name, LocalDate date, Long timeId, Long themeId) {

    public ReservationResponse(Reservation reservation) {
        this(reservation.getId(), reservation.getName(), reservation.getDate(), reservation.getTimeId(),
                reservation.getThemeId());
    }
}
