package roomescape.web.dto;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ThemeTimesResponse(
        Long id,
        LocalTime startAt,
        boolean isReservable
) {
    public static ThemeTimesResponse of(ReservationTime time, boolean isReservable) {
        return new ThemeTimesResponse(time.getId(), time.getStartAt(), isReservable);
    }
}
