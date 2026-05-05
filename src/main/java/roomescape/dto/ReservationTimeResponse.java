package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeResponse(
        Long id,
        LocalTime startAt,
        boolean isAvailable
) {
    public static ReservationTimeResponse from(ReservationTime reservationTime, boolean isAvailable) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                isAvailable);
    }
}
