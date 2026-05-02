package roomescape.controller.dto.response;

import roomescape.service.dto.response.ReservationTimeResult;

import java.time.LocalTime;
import java.util.List;

public record ReservationTimeResponse(
        Long id,
        LocalTime startAt
) {

    public static ReservationTimeResponse from(ReservationTimeResult result) {
        return new ReservationTimeResponse(
                result.id(),
                result.startAt()
        );
    }

    public static List<ReservationTimeResponse> from(List<ReservationTimeResult> results) {
        return results.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }
}
