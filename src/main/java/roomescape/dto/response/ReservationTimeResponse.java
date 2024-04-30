package roomescape.dto.response;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import roomescape.domain.ReservationTime;

public record ReservationTimeResponse(Long id, String startAt) {
    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.id(),
                reservationTime.startAt(DateTimeFormatter.ofPattern("HH:mm"))
        );
    }

    public ReservationTime toReservationTime() {
        return new ReservationTime(id, LocalTime.parse(startAt));
    }
}
