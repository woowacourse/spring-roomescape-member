package roomescape.domain;

import roomescape.exception.InvalidException;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(final Long id, final String name, final LocalDate date, final ReservationTime time) {
        validateEmptyName(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    private void validateEmptyName(final String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidException("이름은 공백일 수 없습니다.");
        }
    }

    public Reservation assignId(final Long id) {
        return new Reservation(id, name, date, time);
    }

    public Reservation assignTime(final ReservationTime time) {
        return new Reservation(id, name, date, time);
    }

    public Long getId() {
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Reservation) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.date, that.date) &&
                Objects.equals(this.time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, time);
    }

    @Override
    public String toString() {
        return "Reservation[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "date=" + date + ", " +
                "time=" + time + ']';
    }

}
