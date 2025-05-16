package roomescape.time.entity;

import roomescape.global.exception.badRequest.BadRequestException;

import java.time.LocalTime;

public class ReservationTime {
    private static final LocalTime OPERATING_START = LocalTime.of(10, 0);
    private static final LocalTime OPERATING_END = LocalTime.of(22, 0);

    private final Long id;
    private LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        if (startAt == null) {
            throw new BadRequestException("필요한 시간 정보가 모두 입력되지 않았습니다.");
        }
        this.id = id;
        this.startAt = startAt;
    }

    public boolean isOnOperatingTime() {
        return !(startAt.isAfter(OPERATING_END) || startAt.isBefore(OPERATING_START));
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
