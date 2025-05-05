package roomescape.reservationTime.domain.dto;

import java.time.LocalTime;

public record ReservationTimeResponseDto(Long id, LocalTime startAt) {
}
