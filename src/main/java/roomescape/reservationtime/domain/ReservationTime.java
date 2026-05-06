package roomescape.reservationtime.domain;

import java.time.LocalTime;
import java.util.Objects;
import roomescape.theme.domain.Theme;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;
    private final Theme theme;

    private ReservationTime(final Long id, final LocalTime startAt, final Theme theme) {
        validateStartAt(startAt);

        this.id = id;
        this.startAt = startAt;
        this.theme = theme;
    }

    public static ReservationTime createNew(final LocalTime startAt, final Theme theme) {
        return new ReservationTime(null, startAt, theme);
    }

    public static ReservationTime of(final Long id, final LocalTime startAt, final Theme theme) {
        validateId(id);
        return new ReservationTime(id, startAt, theme);
    }

    public static void validateId(final Long id) {
        if (id == null) {
            throw new IllegalArgumentException("[ERROR] Id는 비어있을 수 없습니다.");
        }
    }

    private void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("[ERROR] 예약 시간을 비어있을 수 없습니다.");
        }
    }

    public ReservationTime withId(final Long id) {
        validateId(id);
        return new ReservationTime(id, this.startAt, this.theme);
    }

    public Long getId() {
        return this.id;
    }

    public LocalTime getStartAt() {
        return this.startAt;
    }

    public Theme getTheme() {
        return this.theme;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ReservationTime r)) {
            return false;
        }
        return Objects.equals(id, r.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
