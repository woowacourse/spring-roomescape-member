package roomescape.domain.reservationtime;

import java.time.LocalTime;

public class ReservationTimeResponse {

    private Long timeId;
    private LocalTime startAt;

    public ReservationTimeResponse(Long id, LocalTime startAt) {
        this.timeId = id;
        this.startAt = startAt;
    }

    public Long getId() {
        return timeId;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
