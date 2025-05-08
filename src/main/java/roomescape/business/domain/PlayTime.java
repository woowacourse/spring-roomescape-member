package roomescape.business.domain;

import java.time.LocalTime;
import java.util.Objects;

public class PlayTime {

    private Long id;

    private final LocalTime startAt;

    public PlayTime(final LocalTime startAt) {
        this(null, startAt);
    }

    private PlayTime(final Long id, final LocalTime startAt) {
        validateNonNull(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private void validateNonNull(final LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("startAt이 null 입니다.");
        }
    }

    public static PlayTime createWithId(final Long id, final LocalTime startAt) {
        if (id == null) {
            throw new IllegalArgumentException("id가 null 입니다.");
        }

        return new PlayTime(id, startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlayTime playTime = (PlayTime) o;
        return Objects.equals(id, playTime.id) && Objects.equals(startAt, playTime.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startAt);
    }
}
