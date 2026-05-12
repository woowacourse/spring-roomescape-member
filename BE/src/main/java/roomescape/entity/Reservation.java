package roomescape.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ConflictException;
import roomescape.global.exception.customException.DomainRuleViolationException;
import roomescape.global.exception.customException.NotFoundException;

public record Reservation(
        Long id,
        String name,
        LocalDate date,
        ReservationTime time,
        Theme theme
) {
    public static Reservation createWithNullId(String name, LocalDate date, ReservationTime time, Theme theme) {
        return new Reservation(null, name, date, time, theme);
    }

    public static Reservation createWithId(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        return new Reservation(id, name, date, time, theme);
    }

    public Reservation appendId(Long id) {
        return new Reservation(id, name, date, time, theme);
    }

    public void checkOwnership(String name) {
        if (!this.name.equals(name)) {
            throw new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND_BY_NAME);
        }
    }

    public boolean isConflict(Long timeId, Long excludeId) {
        boolean isSameTime = this.time.id().equals(timeId);
        boolean isSameReservation = this.id != null && this.id.equals(excludeId);
        return isSameTime && !isSameReservation;
    }

    public void validateFuture(LocalDateTime now) {
        LocalDateTime reservationDateTime = LocalDateTime.of(this.date, this.time.startAt());
        if (reservationDateTime.isBefore(now)) {
            throw new DomainRuleViolationException(ErrorCode.ILLEGAL_PAST_DATE);
        }
    }

    public void validateUniqueness(List<Reservation> others) {
        boolean isDuplicated = others.stream()
                .anyMatch(other -> other.isConflict(this.time.id(), this.id));

        if (isDuplicated) {
            throw new ConflictException(ErrorCode.RESERVATION_DUPLICATED);
        }
    }

    public Reservation update(Optional<LocalDate> date, Optional<ReservationTime> time) {
        return new Reservation(
                this.id,
                this.name,
                date.orElse(this.date),
                time.orElse(this.time),
                this.theme
        );
    }
}
