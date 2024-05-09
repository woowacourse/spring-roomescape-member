package roomescape.controller.api.dto.response;

import java.util.List;
import roomescape.service.dto.output.ReservationTimeOutput;

public record ReservationTimesResponse(List<ReservationTimeResponse> times) {

    public static ReservationTimesResponse from(final List<ReservationTimeOutput> outputs) {
        return new ReservationTimesResponse(ReservationTimeResponse.list(outputs));
    }
}
