package roomescape.dto.response;

import java.time.format.DateTimeFormatter;

import roomescape.domain.Name;
import roomescape.domain.Reservation;

public record ReservationResponse(Long id, Name name, String date, ReservationTimeResponse time) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.name(),
                reservation.date(DateTimeFormatter.ISO_DATE),
                ReservationTimeResponse.from(reservation.time())
        );
    }
}
