package roomescape.controller.api.dto.response;

import java.util.List;
import roomescape.service.dto.output.ReservationOutput;

public record ReservationResponse(long id, String date, ReservationTimeResponse time, ThemeResponse theme, MemberResponse member) {

    public static ReservationResponse from(final ReservationOutput output) {
        return new ReservationResponse(
                output.id(),
                output.date(),
                ReservationTimeResponse.from(output.time()),
                ThemeResponse.from(output.theme()),
                MemberResponse.from(output.member())
        );
    }

    public static List<ReservationResponse> list(final List<ReservationOutput> outputs) {
        return outputs.stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
