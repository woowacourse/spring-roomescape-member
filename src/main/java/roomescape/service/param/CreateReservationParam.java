package roomescape.service.param;

import java.time.LocalDate;

public record CreateReservationParam(
        String name,
        LocalDate date,
        Long timeId
) {
}
