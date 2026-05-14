package roomescape.controller.dto.reservation;

import java.util.List;

public record AdminReservationsResponse(
        List<AdminReservationResponse> reservations,
        long totalCount,
        int page,
        int size
) {
}
