package roomescape.reservationtime.dto.response;

import roomescape.reservationtime.model.ReservationTime;
import roomescape.util.CustomDateTimeFormatter;

public record FindReservationTimeResponse(Long id, String startAt) {
    public static FindReservationTimeResponse of(final ReservationTime reservationTime) {
        return new FindReservationTimeResponse(
                reservationTime.getId(),
                CustomDateTimeFormatter.getFormattedTime(reservationTime.getTime()));
    }
}
