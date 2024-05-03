package roomescape.reservationtime.dto.response;

import java.time.LocalTime;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.util.CustomDateTimeFormatter;

public record FindReservationTimeResponse(Long id, LocalTime startAt) {
    public static FindReservationTimeResponse of(final ReservationTime reservationTime) {
        return new FindReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getTime());
    }
}
