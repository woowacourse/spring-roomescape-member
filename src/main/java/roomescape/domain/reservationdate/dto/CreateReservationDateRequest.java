package roomescape.domain.reservationdate.dto;

import java.time.LocalDate;
import roomescape.domain.reservationdate.ReservationDate;

public record CreateReservationDateRequest(
    LocalDate playDay
) {

    public ReservationDate toEntity() {
        return ReservationDate.createWithoutId(playDay);
    }
}
