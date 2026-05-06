package roomescape.dto;

import java.time.format.DateTimeFormatter;
import roomescape.domain.ReservationTime;

public record TimeWithStatusResponse(
        Long id,
        String startAt,
        boolean reserved
) {
    public static TimeWithStatusResponse from(ReservationTime reservationTime, boolean reserved) {
        return new TimeWithStatusResponse(reservationTime.getId(), reservationTime.getStartAt().format(
                DateTimeFormatter.ofPattern("HH:mm")), reserved);
    }
}
