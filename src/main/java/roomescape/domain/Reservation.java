package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, new Name(name), date, time, theme);
    }

    public Reservation(Long id, Name name, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.date = Objects.requireNonNull(date, "date must not be null");
        this.time = Objects.requireNonNull(time, "time must not be null");
        this.theme = Objects.requireNonNull(theme, "theme must not be null");
    }

    public boolean hasSameTheme(Reservation reservation) {
        return this.theme.equals(reservation.theme);
    }

    public boolean hasSameDateTime(Reservation reservation) {
        return this.getDateTime().equals(reservation.getDateTime());
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
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

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(this.date, this.time.getStartAt());
    }
}
