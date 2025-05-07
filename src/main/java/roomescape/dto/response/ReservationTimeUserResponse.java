package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeUserResponse(
        long id,
        @JsonFormat(pattern = "HH:mm")
        @Schema(type = "string")
        LocalTime startAt,
        boolean alreadyBooked
) {

    public static ReservationTimeUserResponse from(final ReservationTime time, final boolean alreadyBooked) {
        return new ReservationTimeUserResponse(time.getId(), time.getStartAt(), alreadyBooked);
    }
}
