package roomescape.presentation.dto.admin;

import java.time.LocalDate;
import roomescape.presentation.dto.ReservationRequest;

public record AdminReservationRequest(
        LocalDate date, Long timeId, Long themeId, Long userId
) {

    public ReservationRequest toReservationRequest() {
        return new ReservationRequest(date, timeId, themeId);
    }
}
