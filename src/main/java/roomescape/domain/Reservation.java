package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final User user;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(User user, LocalDate date, ReservationTime time, Theme theme) {
        this(null, user, date, time, theme);
    }

    public Reservation(Long id, User user, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public boolean hasSameTheme(Reservation reservation) {
        return theme.equals(reservation.theme);
    }

    public boolean hasSameDateTime(Reservation reservation) {
        return getDateTime().equals(reservation.getDateTime());
    }

    public boolean isBeforeNow() {
        return getDateTime().isBefore(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
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
        return LocalDateTime.of(date, time.getStartAt());
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
