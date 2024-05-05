package roomescape.dto.reservationtime;

import roomescape.domain.reservationtime.ReservationStartAt;
import roomescape.domain.reservationtime.ReservationTime;

public record ReservationTimeCreateRequest(String startAt) {

    public static ReservationTimeCreateRequest from(String startAt) {
        return new ReservationTimeCreateRequest(startAt);
    }

    public ReservationTime toDomain() {
        return new ReservationTime(
                null,
                ReservationStartAt.from(startAt)
        );
    }
}
