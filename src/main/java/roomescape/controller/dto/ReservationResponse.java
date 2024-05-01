package roomescape.controller.dto;

import java.util.List;
import roomescape.service.dto.output.ReservationOutput;
import roomescape.service.dto.output.ReservationTimeOutput;

public record ReservationResponse(long id, String name, String date, ReservationTimeOutput time) {

    public static ReservationResponse toResponse(ReservationOutput output) {
        return new ReservationResponse(output.id(), output.name(), output.date(), output.time());
    }

    public static List<ReservationResponse> toResponses(List<ReservationOutput> outputs) {
        return outputs.stream()
                .map(ReservationResponse::toResponse)
                .toList();
    }
}
