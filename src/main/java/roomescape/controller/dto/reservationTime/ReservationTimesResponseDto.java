package roomescape.controller.dto.reservationTime;

import java.util.List;

public record ReservationTimesResponseDto(
        List<ReservationTimeResponseDto> times
) {
}
