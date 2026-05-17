package roomescape.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ConflictException;
import roomescape.global.exception.customException.DomainRuleViolationException;
import roomescape.global.exception.customException.ForbiddenException;

public record Reservation(
        Long id,
        String name,
        LocalDate date,
        ReservationTime time,
        Theme theme
) {
    public static Reservation createWithNullId(String name, LocalDate date, ReservationTime time, Theme theme) {
        Reservation newReservation = new Reservation(null, name, date, time, theme);
        newReservation.validateFuture();
        return newReservation;
    }

    public static Reservation createWithId(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        return new Reservation(id, name, date, time, theme);
    }

    public Reservation appendId(Long id) {
        return new Reservation(id, name, date, time, theme);
    }

    public Long timeId() {
        return time.id();
    }

    public Long themeId() {
        return theme.id();
    }

    public void checkOwnership(String name) {
        if (!this.name.equals(name)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }
    }

    public boolean isConflict(Long timeId, Long excludeId) {
        boolean isSameTime = this.timeId().equals(timeId);
        boolean isSameReservation = this.id != null && this.id.equals(excludeId);
        return isSameTime && !isSameReservation;
    }

    public void validateFuture() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(this.date, this.time.startAt());
        if (reservationDateTime.isBefore(now)) {
            throw new DomainRuleViolationException(ErrorCode.ILLEGAL_PAST_DATE);
        }
    }

    public void validateUniqueness(List<Reservation> others) {
        boolean isDuplicated = others.stream()
                .anyMatch(other -> other.isConflict(this.timeId(), this.id));

        if (isDuplicated) {
            throw new ConflictException(ErrorCode.RESERVATION_DUPLICATED);
        }
    }

    public Reservation update(LocalDate newDate, ReservationTime newTime) {
        Reservation updatedReservation = new Reservation(
                this.id,
                this.name,
                newDate,
                newTime,
                this.theme
        );
        updatedReservation.validateFuture();
        return updatedReservation;
    }
}
