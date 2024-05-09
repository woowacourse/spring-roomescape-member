package roomescape.controller.api.dto.response;

import java.util.List;
import roomescape.service.dto.output.ReservationOutput;

public record ReservationsResponse(List<ReservationResponse> reservations) {

    public static ReservationsResponse from(final List<ReservationOutput> outputs) {
        return new ReservationsResponse(ReservationResponse.list(outputs));
    }
}
