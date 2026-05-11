package roomescape.dto.reservationTime;

import java.time.format.DateTimeFormatter;
import roomescape.domain.ReservationTime;

public record ReservationTimeResponse(
    Long id,
    String startAt
) {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        Long id = reservationTime.getId();
        String startAt = TIME_FORMATTER.format(reservationTime.getStartAt());

        return new ReservationTimeResponse(id, startAt);
    }
}
