package roomescape.service.dto.reservation;

import java.time.LocalDate;

public record ChangeReservationScheduleCommand(
        Long reservationId,
        String name,
        LocalDate date,
        Long timeId
) {
}
