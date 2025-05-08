package roomescape.reservation.ui.dto;

import java.time.LocalDate;

public record CreateReservationRequest(
        LocalDate date,
        Long timeId,
        Long themeId
) {

}
