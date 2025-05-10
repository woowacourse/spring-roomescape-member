package roomescape.dto.response;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import roomescape.entity.ReservationTime;

public record ReservationTimeAvailableResponse(
        Long id,
        LocalTime startAt,
        boolean alreadyBooked
) {
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public ReservationTimeAvailableResponse(ReservationTime reservationTime, boolean alreadyBooked) {
        this(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                alreadyBooked
        );
    }

    public String getStartAt() {
        return startAt.format(timeFormatter);
    }
}
