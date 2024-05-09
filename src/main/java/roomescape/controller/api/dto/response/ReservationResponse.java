package roomescape.controller.api.dto.response;

import java.util.List;
import roomescape.service.dto.output.ReservationOutput;

public record ReservationResponse(long id, String name, ThemeResponse theme, String date, ReservationTimeResponse time) {

    public static ReservationResponse from(final ReservationOutput output) {
        return new ReservationResponse(
                output.id(),
                output.name(),
                ThemeResponse.from(output.theme()),
                output.date(),
                ReservationTimeResponse.from(output.time())
        );
    }

    public static List<ReservationResponse> list(final List<ReservationOutput> outputs) {
        return outputs.stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
