package roomescape.application.dto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import roomescape.domain.ReservationTime;

public record ReservationTimeResponse(
        Long id,
        LocalTime startAt
) {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public ReservationTimeResponse(ReservationTime reservationTime) {
        this(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }

    public String getStartAt() {
        return startAt.format(TIME_FORMATTER);
    }
}
