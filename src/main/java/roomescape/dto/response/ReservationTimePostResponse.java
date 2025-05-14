package roomescape.dto.response;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import roomescape.entity.ReservationTime;

public record ReservationTimePostResponse(
        Long id, LocalTime startAt
) {
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public ReservationTimePostResponse(ReservationTime reservationTime) {
        this(
                reservationTime.getId(), reservationTime.getStartAt()
        );
    }

    public String getStartAt() {
        return startAt.format(timeFormatter);
    }
}
