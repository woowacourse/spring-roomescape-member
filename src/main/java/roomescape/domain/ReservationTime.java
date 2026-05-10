package roomescape.domain;

import java.time.LocalTime;
import lombok.Getter;

@Getter
public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    private ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime createNew(LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    public static ReservationTime from(Long id, LocalTime startAt) {
        return new ReservationTime(id, startAt);
    }
}
