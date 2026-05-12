package roomescape.domain;

import java.time.LocalTime;
import java.util.Optional;

public record TimeSlot(Long id, LocalTime startAt) {

    public TimeSlot {
        validate(startAt);
    }

    public static TimeSlot transientOf(LocalTime startAt) {
        return new TimeSlot(null, startAt);
    }

    public TimeSlot patch(LocalTime startAt) {
        return new TimeSlot(
                this.id,
                Optional.ofNullable(startAt).orElse(this.startAt)
        );
    }

    private void validate(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시작 시간은 필수입니다.");
        }
    }
}
