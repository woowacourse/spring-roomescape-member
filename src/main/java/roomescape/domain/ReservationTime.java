package roomescape.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.exception.ReservationTimeNotFoundException;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {

    private final Long id;
    @JsonFormat(pattern = "HH:mm")
    private final LocalTime startAt;

    private ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime from(Long id, LocalTime startAt) {
        return new ReservationTime(id, startAt);
    }

    public static ReservationTime from(LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    public static void validateDeletion(int deleteCount) {
        if (deleteCount == 0) {
            throw new ReservationTimeNotFoundException("해당 시간을 찾을 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ReservationTime that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(startAt, that.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startAt);
    }
}
