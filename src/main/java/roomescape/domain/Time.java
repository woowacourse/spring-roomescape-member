package roomescape.domain;

import java.time.LocalTime;

public class Time {
    private final Long id;
    private final LocalTime startAt;


    public Time(Long id, LocalTime startAt) {
        validate(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private void validate(LocalTime startAt) {
        if (startAt.isBefore(LocalTime.of(10, 0)) || startAt.isAfter(LocalTime.of(22, 0))) {
            throw new IllegalArgumentException("영업 시간은 10시부터 22시 사이입니다.");
        }
    }

    public Time(LocalTime startAt) {
        this(null, startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
