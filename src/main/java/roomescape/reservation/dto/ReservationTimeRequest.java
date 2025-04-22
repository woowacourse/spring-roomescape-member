package roomescape.reservation.dto;

import java.time.LocalTime;

public class ReservationTimeRequest {
    private Long id;
    private LocalTime start_at;

    public ReservationTimeRequest(Long id, LocalTime start_at) {
        this.id = id;
        this.start_at = start_at;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return start_at;
    }
}
