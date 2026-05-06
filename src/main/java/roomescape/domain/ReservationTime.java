package roomescape.domain;

import java.time.LocalTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public static ReservationTime pending(LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    public static ReservationTime create(long id, LocalTime startAt) {
        return new ReservationTime(id, startAt);
    }

    public long id() {
        return id;
    }

    public LocalTime startAt() {
        return startAt;
    }
}
