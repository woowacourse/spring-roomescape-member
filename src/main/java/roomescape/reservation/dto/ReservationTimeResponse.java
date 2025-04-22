package roomescape.reservation.dto;

import java.time.LocalTime;
import roomescape.reservation.entity.ReservationTime;

public class ReservationTimeResponse {
    private long id;
    private LocalTime startAt;

    public ReservationTimeResponse(ReservationTime reservationTime) {
        this.id = reservationTime.getId();
        this.startAt = reservationTime.getStartAt();
    }

    private ReservationTimeResponse() {
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
