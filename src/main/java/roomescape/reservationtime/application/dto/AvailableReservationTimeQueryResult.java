package roomescape.reservationtime.application.dto;

import java.time.LocalTime;
import roomescape.reservationtime.domain.repository.AvailableReservationTime;

public record AvailableReservationTimeQueryResult(
        Long id,
        LocalTime startAt,
        boolean available
) {

    public static AvailableReservationTimeQueryResult from(AvailableReservationTime time) {
        return new AvailableReservationTimeQueryResult(
                time.id(),
                time.startAt(),
                time.available()
        );
    }
}
