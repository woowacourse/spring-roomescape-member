package roomescape.time.presentation.dto;

import java.time.LocalTime;
import lombok.Builder;
import roomescape.time.domain.ReservationTime;

@Builder
public record ReservationTimeResponse(
        Long id,
        LocalTime startAt
) {
    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return ReservationTimeResponse.builder()
                .id(reservationTime.getId())
                .startAt(reservationTime.getStartAt())
                .build();
    }
}
