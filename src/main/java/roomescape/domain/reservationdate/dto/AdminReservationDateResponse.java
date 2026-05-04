package roomescape.domain.reservationdate.dto;

import java.time.LocalDate;
import roomescape.domain.reservationdate.ReservationDate;

public record AdminReservationDateResponse(
    Long id,
    LocalDate reservationDate
) {

    public static AdminReservationDateResponse from(ReservationDate reservationDate) {
        return new AdminReservationDateResponse(reservationDate.getId(), reservationDate.getDate());
    }
}
