package roomescape.controller.dto.response;

import roomescape.service.dto.response.ReservationResult;

import java.time.LocalDate;
import java.util.List;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time
) {

    public static ReservationResponse from(ReservationResult result) {
        return new ReservationResponse(
                result.id(),
                result.name(),
                result.date(),
                ReservationTimeResponse.from(result.time())
        );
    }

    public static List<ReservationResponse> from(List<ReservationResult> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
