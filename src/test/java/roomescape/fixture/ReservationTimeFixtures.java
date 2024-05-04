package roomescape.fixture;

import roomescape.domain.reservationtime.ReservationStartAt;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.dto.reservationtime.ReservationTimeCreateRequest;

public class ReservationTimeFixtures {

    private ReservationTimeFixtures() {
    }

    public static ReservationTime createReservationTime(String startAt) {
        return new ReservationTime(null, ReservationStartAt.from(startAt));
    }

    public static ReservationTimeCreateRequest createReservationTimeCreateRequest(String startAt) {
        return ReservationTimeCreateRequest.from(startAt);
    }
}
