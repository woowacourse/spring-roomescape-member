package roomescape.domain.reservation.domain;

import roomescape.global.exception.RoomEscapeException;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        checkNull(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private void checkNull(LocalTime startAt) {
        try {
            Objects.requireNonNull(startAt, "[ERROR] 시작 시간은 null일 수 없습니다.");
        } catch (NullPointerException e) {
            throw new RoomEscapeException(e.getMessage());
        }
    }

    public boolean inPast() {
        return startAt.isBefore(LocalTime.now());
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
