package roomescape.dto.response;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import roomescape.domain.ReservationTime;

public record ReservationTimeResponse(Long id, String startAt, Boolean alreadyBooked) {
    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt(DateTimeFormatter.ofPattern("HH:mm")),
                reservationTime.getAlreadyBooked()
        );
    }

    public ReservationTime toReservationTime() {
        return new ReservationTime(id, LocalTime.parse(startAt));
    }
}
