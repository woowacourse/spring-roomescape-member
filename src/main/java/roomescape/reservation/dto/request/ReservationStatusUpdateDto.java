package roomescape.reservation.dto.request;

import roomescape.reservation.domain.ReservationStatus;

public record ReservationStatusUpdateDto(
        ReservationStatus status
) {
}
