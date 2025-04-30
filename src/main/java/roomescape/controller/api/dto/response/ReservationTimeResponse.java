package roomescape.controller.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

import roomescape.service.dto.response.ReservationTimeServiceResponse;

public record ReservationTimeResponse(
        Long id,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime startAt
) {

    public static ReservationTimeResponse from(ReservationTimeServiceResponse query) {
        return new ReservationTimeResponse(query.id(), query.startAt());
    }
}
