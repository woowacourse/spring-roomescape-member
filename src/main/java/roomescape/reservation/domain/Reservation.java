package roomescape.reservation.domain;

import roomescape.exception.BusinessRuleException;
import roomescape.exception.ErrorCode;
import roomescape.exception.OwnershipViolationException;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {

    public void validateOwner(String name) {
        if (!this.name.equals(name)) {
            throw new OwnershipViolationException(ErrorCode.OWNERSHIP_VIOLATION, "예약자 이름이 일치하지 않습니다.");
        }
    }

    public void validateIsActive() {
        if (LocalDateTime.of(date, time.startAt()).isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException(ErrorCode.EXPIRED_RESERVATION, "이미 지난 예약은 취소하거나 변경할 수 없습니다.");
        }
    }

    public boolean hasSameDateAndTime(LocalDate newDate, long newTimeId) {
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
