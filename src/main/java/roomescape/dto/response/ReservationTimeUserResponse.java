package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeUserResponse(
        @Schema(description = "예약 시간 ID", example = "1")
        long id,

        @JsonFormat(pattern = "HH:mm")
        @Schema(type = "string", description = "예약 시간", example = "10:00")
        LocalTime startAt,

        @Schema(description = "예약 체크", example = "true")
        boolean alreadyBooked
) {

    public static ReservationTimeUserResponse from(final ReservationTime time, final boolean alreadyBooked) {
        return new ReservationTimeUserResponse(time.getId(), time.getStartAt(), alreadyBooked);
    }
}
