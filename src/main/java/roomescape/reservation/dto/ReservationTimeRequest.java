package roomescape.reservation.dto;

import java.time.LocalTime;

public class ReservationTimeRequest {
    private Long id;
    private LocalTime startAt;

    public ReservationTimeRequest(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
