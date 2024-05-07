package roomescape.model;

import java.time.LocalDate;
import java.util.Objects;

import roomescape.exception.BadRequestException;

public class Reservation {

    private Long id;
    private String name;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;

    private Reservation() {
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validateNullOrBlank(name, "name");
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    private void validateNullOrBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException(value, fieldName);
        }
    }

    public long getId() {
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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Reservation that = (Reservation) object;
        return Objects.equals(getId(), that.getId())
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getDate(), that.getDate())
                && Objects.equals(getTime(), that.getTime())
                && Objects.equals(getTheme(), that.getTheme());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDate(), getTime(), getTheme());
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", theme=" + theme +
                '}';
    }
}
