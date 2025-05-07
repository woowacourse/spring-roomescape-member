package roomescape.reservationtime.domain;

import java.time.LocalTime;
import roomescape.reservationtime.exception.ReservationTimeFieldRequiredException;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    private ReservationTime(Long id, LocalTime startAt) {
        validateStartAt(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime createWithoutId(LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    public static ReservationTime createWithId(Long id, LocalTime startAt) {
        return new ReservationTime(id, startAt);
    }

    private void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new ReservationTimeFieldRequiredException();
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
