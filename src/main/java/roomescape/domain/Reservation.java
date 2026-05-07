package roomescape.domain;

import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private final ReservationId id;
    private final ReservationName name;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        this(
                id == null ? null : new ReservationId(id),
                new ReservationName(name),
                new ReservationDate(date),
                requireTime(time),
                requireTheme(theme)
        );
    }

    private Reservation(ReservationId id, ReservationName name, ReservationDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation withId(Long id) {
        ReservationId reservationId = new ReservationId(id);

        if (this.id != null) {
            throw new DomainException(ErrorCode.RESERVATION_ALREADY_HAS_ID);
        }

        return new Reservation(reservationId, name, date, time, theme);
    }

    private static ReservationTime requireTime(ReservationTime time) {
        if (time == null) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_TIME);
        }
        return time;
    }

    private static Theme requireTheme(Theme theme) {
        if (theme == null) {
            throw new DomainException(ErrorCode.INVALID_THEME);
        }
        return theme;
    }

    public Long getId() {
        if (id == null) {
            return null;
        }
        return id.value();
    }

    public String getName() {
        return name.value();
    }

    public LocalDate getDate() {
        return date.value();
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public boolean isSameTime(ReservationTime time) {
        return Objects.equals(this.time, time);
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
}
