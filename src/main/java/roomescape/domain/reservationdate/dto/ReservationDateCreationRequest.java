package roomescape.domain.reservationdate.dto;

import java.time.LocalDate;
import roomescape.domain.reservationdate.ReservationDate;

public record ReservationDateCreationRequest(
    LocalDate playDay
) {

    public ReservationDate toEntity() {
        return ReservationDate.createWithoutId(playDay);
    }
}
