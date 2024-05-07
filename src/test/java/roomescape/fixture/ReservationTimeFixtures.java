package roomescape.fixture;

import roomescape.domain.reservationtime.ReservationStartAt;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.dto.reservationtime.ReservationTimeCreateRequest;
import roomescape.dto.reservationtime.ReservationTimeResponse;

public class ReservationTimeFixtures {

    private ReservationTimeFixtures() {
    }

    public static ReservationTime createReservationTime(String startAt) {
        return new ReservationTime(null, ReservationStartAt.from(startAt));
    }

    public static ReservationTimeCreateRequest createReservationTimeCreateRequest(String startAt) {
        return ReservationTimeCreateRequest.from(startAt);
    }

    public static ReservationTimeResponse createReservationTimeResponse(long id, String startAt) {
        return ReservationTimeResponse.of(id, startAt);
    }
}
