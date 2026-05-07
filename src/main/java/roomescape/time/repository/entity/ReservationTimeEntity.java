package roomescape.time.repository.entity;

import java.time.LocalTime;
import lombok.Getter;

@Getter
public class ReservationTimeEntity {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTimeEntity(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }
}
