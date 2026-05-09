package roomescape.domain.reservationdate.dto;

import java.time.LocalDate;
import roomescape.domain.reservationdate.ReservationDate;

public record CreateReservationDateResponse(
    Long id,
    LocalDate playDay
) {

    public static CreateReservationDateResponse from(ReservationDate reservationDate) {
        return new CreateReservationDateResponse(reservationDate.getId(), reservationDate.getPlayDay());
    }
}
