package roomescape.reservation.dto.response;

import java.time.LocalTime;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.util.CustomDateTimeFormatter;

public record FindTimeOfReservationsResponse(Long id, LocalTime startAt) {
    public static FindTimeOfReservationsResponse of(final ReservationTime reservationTime) {
        return new FindTimeOfReservationsResponse(
                reservationTime.getId(),
                reservationTime.getTime()
        );
    }
}
