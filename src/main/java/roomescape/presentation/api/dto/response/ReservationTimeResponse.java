package roomescape.presentation.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.application.dto.response.ReservationTimeServiceResponse;

public record ReservationTimeResponse(
        Long id,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime startAt,
        boolean isBooked
) {

    public static ReservationTimeResponse from(ReservationTimeServiceResponse response) {
        return new ReservationTimeResponse(response.id(), response.startAt(), response.isBooked());
    }
}
