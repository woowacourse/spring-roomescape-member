package roomescape.controller.dto;

import java.util.List;
import roomescape.service.dto.output.ReservationTimeOutput;

public record ReservationTimeResponse(long id, String startAt) {

    public static ReservationTimeResponse toResponse(ReservationTimeOutput output) {
        return new ReservationTimeResponse(output.id(), output.startAt());
    }

    public static List<ReservationTimeResponse> toResponses(List<ReservationTimeOutput> outputs) {
        return outputs.stream()
                .map(ReservationTimeResponse::toResponse)
                .toList();
    }
}
