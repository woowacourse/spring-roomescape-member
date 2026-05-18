package roomescape.domain;

import roomescape.domain.exception.DomainRuleViolationException;

import java.time.LocalTime;
import java.util.Objects;

public class Time {
    private final Long id;
    private final LocalTime startAt;

    public Time(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static Time create(LocalTime startAt) {
        validate(startAt);
        return new Time(null, startAt);
    }

    private static void validate(LocalTime startAt) {
        if (startAt.isBefore(LocalTime.of(10, 0)) || startAt.isAfter(LocalTime.of(22, 0))) {
            throw new DomainRuleViolationException("영업 시간은 10시부터 22시 사이입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
