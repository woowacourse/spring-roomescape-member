package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeUserResponse(
        long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,
        boolean alreadyBooked
) {

    public static ReservationTimeUserResponse from(ReservationTime time, boolean alreadyBooked) {
        return new ReservationTimeUserResponse(time.getId(), time.getStartAt(), alreadyBooked);
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
