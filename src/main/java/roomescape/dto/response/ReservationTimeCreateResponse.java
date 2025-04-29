package roomescape.dto.response;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public class ReservationTimeCreateResponse {

    private final long id;
    private final LocalTime startAt;

    public ReservationTimeCreateResponse(ReservationTime reservationTime) {
        this.id = reservationTime.getId();
        this.startAt = reservationTime.getStartAt();
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
