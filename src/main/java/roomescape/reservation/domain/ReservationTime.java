package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.reservation.exception.PastReservationException;

public class ReservationTime {
    private final Long id;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    public ReservationTime(LocalDateTime startAt, LocalDateTime endAt) {
        this(null, startAt, endAt);
    }

    public ReservationTime(Long id, LocalDateTime startAt, LocalDateTime endAt) {
        this.id = id;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public ReservationTime withId(Long id) {
        return new ReservationTime(id, startAt, endAt);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public LocalDate getDate() {
        return startAt.toLocalDate();
    }

    public void validateReservableSchedule() {
        if (isPast()) {
            throw PastReservationException.pastReservation();
        }
    }

    public void validateUpdatableReservation() {
        if (isPast()) {
            throw PastReservationException.pastUpdate();
        }
    }

    private boolean isPast() {
        return startAt.isBefore(LocalDateTime.now());
    }

    public void validateNotPastForCancel() {
        if (startAt.isBefore(LocalDateTime.now())) {
            throw PastReservationException.pastCancel();
        }
    }
}
