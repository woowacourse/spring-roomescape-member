package roomescape.model;

import java.time.LocalDate;
import java.util.Objects;

import roomescape.exception.BadRequestException;

public class Reservation {

    private Long id;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;
    private User user;

    private Reservation() {
    }

    public Reservation(Long id, LocalDate date, ReservationTime time, Theme theme, User user) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.user = user;
    }

    public Reservation(LocalDate date, ReservationTime time, Theme theme, User user) {
        this(null, date, time, theme, user);
    }

    public long getId() {
        return id;
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

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Reservation that = (Reservation) object;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getDate(), that.getDate())
                && Objects.equals(getTime(), that.getTime()) && Objects.equals(getTheme(),
                that.getTheme()) && Objects.equals(getUser(), that.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate(), getTime(), getTheme(), getUser());
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", theme=" + theme +
                ", user=" + user +
                '}';
    }
}
