package roomescape.domain;

import java.time.LocalTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;
    private final TimeStatus status;

    public static ReservationTime pending(LocalTime startAt) {
        return new ReservationTime(null, startAt, TimeStatus.DRAFT);
    }

    public static ReservationTime of(long id, LocalTime startAt) {
        return new ReservationTime(id, startAt, TimeStatus.AVAILABLE);
    }

    public ReservationTime deleted() {
        return new ReservationTime(this.id, this.startAt, TimeStatus.DELETED);
    }

    public long id() {
        return id;
    }

    public LocalTime startAt() {
        return startAt;
    }

    public TimeStatus status() {
        return status;
    }
}
