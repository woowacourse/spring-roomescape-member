package roomescape.controller.api.dto.response;

import java.util.List;
import roomescape.service.dto.output.ReservationTimeOutput;

public record ReservationTimeResponse(long id, String startAt) {

    public static ReservationTimeResponse toResponse(final ReservationTimeOutput output) {
        return new ReservationTimeResponse(output.id(), output.startAt());
    }

    public static List<ReservationTimeResponse> toResponses(final List<ReservationTimeOutput> outputs) {
        return outputs.stream()
                .map(ReservationTimeResponse::toResponse)
                .toList();
    }
}
