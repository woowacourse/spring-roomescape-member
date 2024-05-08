package roomescape.time.dto;

import roomescape.time.domain.ReservationTime;

public record ReservationTimeResponseDto(
        long id,
        String startAt
) {
    public ReservationTimeResponseDto(final ReservationTime reservationTime) {
        this(
                reservationTime.getId(),
                reservationTime.getStartAt().toString()
        );
    }
}
