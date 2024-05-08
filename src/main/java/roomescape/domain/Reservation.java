package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {
    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(
            final Long id,
            final Name name,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(final Long reservationId, final String name, final LocalDate date, final ReservationTime time, final Theme theme) {
        this(reservationId, new Name(name), date, time, theme);
    }

    public boolean isBeforeThan(final LocalDateTime otherTime) {
        LocalDateTime reservationTime = LocalDateTime.of(date, time.getStartAt());
        return reservationTime.isBefore(otherTime);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
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

    public long getTimeId() {
        return time.getId();
    }

    public long getThemeId() {
        return theme.getId();
    }
}
