package roomescape.time.domain;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    private ReservationTime(Long id, LocalTime startAt) {
        validateStartAt(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime create(LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    public static ReservationTime load(Long timeId, LocalTime startAt) {
        validateId(timeId);
        return new ReservationTime(timeId, startAt);
    }

    private static void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("예약 시작 시간은 필수입니다.");
        }
    }

    private static void validateId(Long timeId) {
        if (timeId == null) {
            throw new IllegalArgumentException("예약 시간 ID는 필수입니다.");
        }
    }

    public Long id() {
        return id;
    }

    public LocalTime startAt() {
        return startAt;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ReservationTime that)) {
            return false;
        }

        if (that.id == null || this.id == null) {
            return false;
        }

        return Objects.equals(this.id, that.id);
    }
}
