package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeWithBookStatusResponse(
        Long id,
        @JsonFormat(pattern = TIME_FORMAT) LocalTime startAt,
        boolean booked) {
    private static final String TIME_FORMAT = "HH:mm";

    public static ReservationTimeWithBookStatusResponse fromReservationTime(
            ReservationTime reservationTime, boolean booked) {
        return new ReservationTimeWithBookStatusResponse(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                booked);
    }
}
