package roomescape.dto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import roomescape.domain_entity.ReservationTime;

public record ReservationTimeResponse(
        Long id, LocalTime startAt
) {
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt());
    }

    public String getStartAt() {
        return startAt.format(timeFormatter);
    }
}
