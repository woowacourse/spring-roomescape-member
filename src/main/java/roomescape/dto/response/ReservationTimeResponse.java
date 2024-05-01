package roomescape.dto.response;

import java.time.format.DateTimeFormatter;
import roomescape.domain.ReservationTime;

public record ReservationTimeResponse(Long id, String startAt) {
    private static final String TIME_FORMAT = "HH:mm";

    public ReservationTimeResponse(ReservationTime reservationTime) {
        this(reservationTime.getId(),
                reservationTime.getStartAt().format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
    }
}
