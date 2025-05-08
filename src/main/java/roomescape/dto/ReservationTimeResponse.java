package roomescape.dto;

import roomescape.model.ReservationTime;

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
