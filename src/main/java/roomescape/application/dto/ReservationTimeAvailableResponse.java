package roomescape.application.dto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import roomescape.domain.ReservationTime;

public record ReservationTimeAvailableResponse(
        Long id,
        LocalTime startAt,
        boolean alreadyBooked
) {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public ReservationTimeAvailableResponse(ReservationTime reservationTime, boolean alreadyBooked) {
        this(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                alreadyBooked
        );
    }

    public String getStartAt() {
        return startAt.format(TIME_FORMATTER);
    }
}
