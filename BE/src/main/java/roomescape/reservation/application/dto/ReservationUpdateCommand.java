package roomescape.reservation.application.dto;

import java.time.LocalDate;


public record ReservationUpdateCommand(
    Long id,
    LocalDate date,
    Long timeId,
    String name
) {
}
