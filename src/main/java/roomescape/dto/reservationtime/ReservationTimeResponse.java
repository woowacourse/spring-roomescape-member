package roomescape.dto.reservationtime;

import roomescape.domain.reservationtime.ReservationStartAt;
import roomescape.domain.reservationtime.ReservationTime;

public record ReservationTimeResponse(Long id, String startAt) {

    public static ReservationTimeResponse from(ReservationTime time) {
        ReservationStartAt reservationStartAt = time.getStartAt();
        return new ReservationTimeResponse(
                time.getId(),
                reservationStartAt.toStringTime()
        );
    }

    public static ReservationTimeResponse of(Long id, String startAt) {
        return new ReservationTimeResponse(id, startAt);
    }
}
