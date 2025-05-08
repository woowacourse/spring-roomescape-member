package roomescape.presentation.dto;

import java.time.LocalTime;
import roomescape.business.domain.reservation.ReservationTime;

public record ReservationTimeResponseDto(
        long id,
        LocalTime startAt
) {

    public static ReservationTimeResponseDto toResponse(ReservationTime reservationTime) {
        return new ReservationTimeResponseDto(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }
}
