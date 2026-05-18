package roomescape.controller.dto.reservation;

import java.util.List;

public record ReservationsResponseDto(
        List<ReservationResponseDto> reservations
) {
}
