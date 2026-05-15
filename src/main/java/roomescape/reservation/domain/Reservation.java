package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.exception.BusinessRuleException;
import roomescape.exception.OwnershipViolationException;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public void validateOwner(String name) {
        if (!this.name.equals(name)) {
            throw new OwnershipViolationException("예약자 이름이 일치하지 않습니다.");
        }
    }

    public void validateIsActive() {
        if (LocalDateTime.of(date, time.startAt()).isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("이미 지난 예약은 취소하거나 변경할 수 없습니다.");
        }
    }

    public boolean isSameSlot(LocalDate newDate, long newTimeId) {
        return this.date.equals(newDate) && this.time.id().equals(newTimeId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation other)) {
            return false;
        }
        if (this.id == null || other.id == null) {
            return false;
        }
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return System.identityHashCode(this);
        }
        return id.hashCode();
    }
}
