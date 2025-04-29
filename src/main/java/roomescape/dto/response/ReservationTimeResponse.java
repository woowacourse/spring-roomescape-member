package roomescape.dto.response;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public class ReservationTimeResponse {

    private final long id;
    private final LocalTime startAt;

    public ReservationTimeResponse(ReservationTime time) {
        this.id = time.getId();
        this.startAt = time.getStartAt();
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
