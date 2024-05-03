package roomescape.reservation.dto.response;

import java.time.LocalTime;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.util.CustomDateTimeFormatter;

public record CreateTimeOfReservationsResponse(Long id, LocalTime startAt) {
    public static CreateTimeOfReservationsResponse of(final ReservationTime reservationTime) {
        return new CreateTimeOfReservationsResponse(
                reservationTime.getId(),
                reservationTime.getTime()
        );
    }
}
