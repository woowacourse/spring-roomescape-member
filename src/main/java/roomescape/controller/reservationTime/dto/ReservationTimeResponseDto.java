package roomescape.controller.reservationTime.dto;

import java.time.LocalTime;
import roomescape.model.ReservationTime;

public record ReservationTimeResponseDto(
        Long id,
        LocalTime startAt
) {
    public static ReservationTimeResponseDto from(ReservationTime reservationTime) {
        return new ReservationTimeResponseDto(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }
}
