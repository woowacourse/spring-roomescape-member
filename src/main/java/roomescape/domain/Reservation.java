package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final Name name;
    private final ReservationDate date;
    private final ReservationTime time;

    public Reservation(Long id, Name name, ReservationDate date, ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation(Long id, String name, String date, Long timeId, String time) {
        this(id, new Name(name), new ReservationDate(date), new ReservationTime(timeId, time));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public LocalDate getDate() {
        return date.getDate();
    }

    public ReservationTime getTime() {
        return time;
    }

    public Long getTimeId() {
        return time.getId();
    }

    public LocalTime getTimeStartAt() {
        return time.getStartAt();
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
        return Objects.hashCode(id);
    }
}
