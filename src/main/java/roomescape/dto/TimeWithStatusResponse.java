package roomescape.dto;

import roomescape.domain.ReservationTime;
import roomescape.utils.DateTimeConverter;

public record TimeWithStatusResponse(
        Long id,
        String startAt,
        boolean reserved
) {
    public static TimeWithStatusResponse from(ReservationTime reservationTime, boolean reserved) {
        return new TimeWithStatusResponse(
                reservationTime.getId(),
                reservationTime.getStartAt().format(DateTimeConverter.TIME_FORMATTER),
                reserved
        );
    }
}
