package roomescape.reservationtime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.reservationtime.domain.ReservationTime;

public record ReservationTimeUserResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,
        boolean alreadyBooked
) {

    public static ReservationTimeUserResponse from(ReservationTime time, boolean alreadyBooked) {
        return new ReservationTimeUserResponse(time.getId(), time.getStartAt(), alreadyBooked);
    }
}
