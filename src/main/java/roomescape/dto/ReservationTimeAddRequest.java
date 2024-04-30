package roomescape.dto;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public class ReservationTimeAddRequest {
    private LocalTime startAt;

    public ReservationTimeAddRequest() {

    }

    public ReservationTime toEntity() {
        return new ReservationTime(null, startAt);
    }

    public ReservationTimeAddRequest(LocalTime startAt) {
        this.startAt = startAt;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
