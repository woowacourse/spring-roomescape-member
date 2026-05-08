package roomescape.dto.reservationTime;

import java.time.format.DateTimeFormatter;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;

public record AvailableReservationTimeResponse(long id, String startAt, boolean isAvailable) {
    public static AvailableReservationTimeResponse from(ReservationTimeWithAvailable reservationTimeWithAvailable) {
        return new AvailableReservationTimeResponse(
                reservationTimeWithAvailable.id(),
                reservationTimeWithAvailable.startAt().format(DateTimeFormatter.ofPattern("HH:mm")),
                reservationTimeWithAvailable.isAvailable()
        );
    }
}
