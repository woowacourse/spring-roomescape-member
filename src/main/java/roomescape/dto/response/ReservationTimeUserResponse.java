package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

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
