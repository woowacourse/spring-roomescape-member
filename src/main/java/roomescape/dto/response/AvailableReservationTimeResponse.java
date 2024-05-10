package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record AvailableReservationTimeResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm") LocalTime startAt,
        Boolean alreadyBooked
) {
    public static AvailableReservationTimeResponse from(ReservationTime reservationTime, boolean alreadyBooked) {
        return new AvailableReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                alreadyBooked);
    }
}
