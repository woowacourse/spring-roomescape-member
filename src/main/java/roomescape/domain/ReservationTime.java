package roomescape.domain;

import roomescape.domain.exception.InvalidRequestException;
import roomescape.domain.exception.InvalidTimeException;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(final Long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(final Long id, final String startAt) { //TODO 어떤건 this() 어떤건 this. 뭐시기로,,, 통일해보기
        validateNull(startAt);
        this.id = id;
        this.startAt = validateFormatAndConvert(startAt);
    }

    public ReservationTime(final String startAt) {
        this(null, startAt);
    }

    private void validateNull(final String startAt) {
        if (startAt == null || startAt.isBlank()) {
            throw new InvalidRequestException("공백일 수 없습니다.");
        }
    }

    private LocalTime validateFormatAndConvert(final String startAt) {
        try {
            return LocalTime.parse(startAt);
        } catch (DateTimeException e) {
            throw new InvalidTimeException("유효하지 않은 시간 입니다.");
        }
    }

    public ReservationTime assignId(final Long id) {
        return new ReservationTime(id, startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(final Object target) {
        if (this == target) {
            return true;
        }
        if (target == null || getClass() != target.getClass()) {
            return false;
        }
        final ReservationTime that = (ReservationTime) target;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "ReservationTime[" +
                "id=" + id + ", " +
                "startAt=" + startAt + ']';
    }
}
