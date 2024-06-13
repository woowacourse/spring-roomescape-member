package roomescape.dto.reservation;

import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.time.ReservationTime;

public record AdminReservationResponse(long id, String memberName, String themeName, LocalDate date,
                                       ReservationTime time) {

    public static AdminReservationResponse from(Reservation reservation) {
        return new AdminReservationResponse(
                reservation.getId(),
                reservation.getMember().getName().getValue(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime()
        );
    }

}
