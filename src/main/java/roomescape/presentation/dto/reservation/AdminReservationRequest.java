package roomescape.presentation.dto.reservation;

import java.time.LocalDate;

public record AdminReservationRequest(
        LocalDate date, Long timeId, Long themeId, Long userId
) {

    public ReservationRequest toReservationRequest() {
        return new ReservationRequest(date, timeId, themeId);
    }
}
