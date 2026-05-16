package roomescape.reservation.application.dto;

import java.time.LocalDate;

public record ReservationUpdateCommand(
        LocalDate date,
        Long timeId
) {
}
