package roomescape.dto.reservationTime;

import java.util.List;

public record ReservationTimesResponseDto(
        List<ReservationTimeResponseDto> times
) {
}
