package roomescape.reservationtime.domain;

import java.time.LocalTime;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    private ReservationTime(final Long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime createNew(final LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    public static ReservationTime of(final Long id, final LocalTime startAt) {
        validateId(id);
        return new ReservationTime(id, startAt);
    }

    public ReservationTime withId(final Long id) {
        validateId(id);
        return new ReservationTime(id, this.startAt);
    }

    public static void validateId(final Long id){
        if(id == null) {
            throw new IllegalArgumentException("[ERROR] Id는 비어있을 수 없습니다.");
        }
    }

    public Long getId() {
        return this.id;
    }

    public LocalTime getStartAt() {
        return this.startAt;
    }

}
