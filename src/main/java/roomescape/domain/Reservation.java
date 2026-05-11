package roomescape.domain;

import lombok.Getter;
import roomescape.common.exception.DomainException;
import roomescape.common.exception.ErrorCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validateName(name);
        validateDate(date);
        validateTime(time);
        validateTheme(theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation withId(Long id) {
        validateId(id);
        if (this.id != null) {
            throw new DomainException(ErrorCode.RESERVATION_ALREADY_HAS_ID);
        }

        return new Reservation(id, name, date, time, theme);
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_ID);
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_NAME);
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_DATE);
        }
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_TIME);
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new DomainException(ErrorCode.INVALID_THEME);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public boolean isPast(LocalDateTime now) {
        return LocalDateTime.of(date, time.getStartAt())
                .isBefore(now);
    }
}
