package roomescape.repository.projection;

import roomescape.domain.ReservationTime;

public class ReservationTimeEntity {

    private final Long id;
    private final ReservationTime time;

    public ReservationTimeEntity(Long id, ReservationTime time) {
        this.id = id;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public ReservationTime getTime() {
        return time;
    }
}
