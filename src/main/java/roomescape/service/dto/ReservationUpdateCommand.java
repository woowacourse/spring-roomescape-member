package roomescape.service.dto;

import java.time.LocalDate;
import roomescape.domain.EntityId;

public record ReservationUpdateCommand(
        EntityId reservationId,
        String name,
        LocalDate date,
        EntityId timeId
) {
}
