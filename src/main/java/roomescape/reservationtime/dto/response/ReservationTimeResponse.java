package roomescape.reservationtime.dto.response;

import roomescape.reservationtime.domain.ReservationTime;

public record ReservationTimeResponse(
        Long id,
        String startAt
) {
    public static ReservationTimeResponse fromEntity(ReservationTime time) {
        return new ReservationTimeResponse(
                time.getId(),
                time.getStartAt().toString()
        );
    }
}
