package roomescape.web.dto.theme;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeStatusResponse(
        Long id,
        LocalTime startAt,
        boolean isReservable
) {
    public static ReservationTimeStatusResponse of(ReservationTime time, boolean isReservable) {
        return new ReservationTimeStatusResponse(time.getId(), time.getStartAt(), isReservable);
    }
}
