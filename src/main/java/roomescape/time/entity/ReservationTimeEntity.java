package roomescape.time.entity;

import roomescape.exception.BadRequestException;

import java.time.LocalTime;

public class ReservationTimeEntity {
    private static final LocalTime RUNNING_TIME = LocalTime.of(2, 0);
    private static final LocalTime OPERATING_START = LocalTime.of(10, 0);
    private static final LocalTime OPERATING_END = LocalTime.of(22, 0);

    private final Long id;
    private LocalTime startAt;

    public ReservationTimeEntity(Long id, LocalTime startAt) {
        if (id == null || startAt == null) {
            throw new BadRequestException("필요한 시간 정보가 모두 입력되지 않았습니다.");
        }
        this.id = id;
        this.startAt = startAt;
    }

    public boolean isDuplicatedWith(ReservationTimeEntity other) {
        LocalTime otherStartAt = other.startAt;
        final int interval = Math.abs(otherStartAt.toSecondOfDay() - startAt.toSecondOfDay());
        return interval < RUNNING_TIME.toSecondOfDay();
    }

    public boolean isAvailable() {
        return !(startAt.isBefore(OPERATING_START) || startAt.isAfter(OPERATING_END));
    }

    public String getFormattedTime() {
        return startAt.toString();
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
