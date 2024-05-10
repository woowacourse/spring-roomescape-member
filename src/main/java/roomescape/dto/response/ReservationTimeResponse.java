package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeResponse(
        Long id,
        @JsonFormat(pattern = TIME_FORMAT) LocalTime startAt) {
    private static final String TIME_FORMAT = "HH:mm";

    public static ReservationTimeResponse fromReservationTime(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt());
    }
}
