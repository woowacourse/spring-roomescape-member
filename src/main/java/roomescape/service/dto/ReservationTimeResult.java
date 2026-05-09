package roomescape.service.dto;

import java.time.LocalTime;
import roomescape.repository.projection.ReservationTimeEntity;

public class ReservationTimeResult {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTimeResult(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTimeResult from(ReservationTimeEntity entity) {
        return new ReservationTimeResult(
                entity.getId(),
                entity.getTime().getStartAt()
        );
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
