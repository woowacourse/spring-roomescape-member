package roomescape.domain;

import java.time.LocalTime;
import java.util.Optional;

public class TimeSlot {

    private Long id;
    private LocalTime startAt;

    public TimeSlot(Long id, LocalTime startAt) {
        validate(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public static TimeSlot transientOf(LocalTime startAt) {
        return new TimeSlot(null, startAt);
    }

    public void patch(LocalTime startAt) {
        this.startAt = Optional.ofNullable(startAt).orElse(this.startAt);
    }

    private void validate(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시작 시간은 필수입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
