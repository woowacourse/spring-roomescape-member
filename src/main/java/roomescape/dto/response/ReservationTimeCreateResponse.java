package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeCreateResponse(
        long id,
        @JsonFormat(pattern = "HH:mm")
        @Schema(type = "string")
        LocalTime startAt
) {

    public static ReservationTimeCreateResponse from(ReservationTime reservationTime) {
        return new ReservationTimeCreateResponse(reservationTime.getId(), reservationTime.getStartAt());
    }
}
