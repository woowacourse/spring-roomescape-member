package roomescape.presentation.dto;

import java.time.LocalDate;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;

public record ReservationRequest(
        String name, LocalDate date, Long timeId
) {

    public Reservation toDomain(final PlayTime playTime) {
        return new Reservation(name, date, playTime);
    }
}
