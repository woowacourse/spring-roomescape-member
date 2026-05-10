package roomescape.time.repository.entity;

import java.time.LocalTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReservationTimeEntity {

    @EqualsAndHashCode.Include
    private final Long id;
    private final LocalTime startAt;

    public ReservationTimeEntity(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }
}
