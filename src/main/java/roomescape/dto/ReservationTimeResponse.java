package roomescape.dto;

import roomescape.domain.ReservationTime;
import roomescape.utils.DateTimeConverter;

public record ReservationTimeResponse(
        Long id,
        String startAt
) {
    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt().format(DateTimeConverter.TIME_FORMATTER)
        );
    }
}
