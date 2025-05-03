package roomescape.persistence.query;

import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public record CreateReservationQuery(
        String name,
        LocalDate date,
        ReservationTime time,
        Theme theme
) {
}
