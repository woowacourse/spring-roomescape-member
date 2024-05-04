package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponse(Long id, String name, LocalDate date, Long timeId, Long themeId) {
    public ReservationResponse(Reservation reservation) {
        this(reservation.getId(), reservation.getName(), reservation.getDate(), reservation.getTimeId(),
                reservation.getThemeId());
    }
}
