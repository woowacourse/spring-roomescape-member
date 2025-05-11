package roomescape.reservation.controller.dto;

import java.time.LocalDate;

public record CreateReservationRequest(
        LocalDate date,
        Long themeId,
        Long timeId
) {

}
