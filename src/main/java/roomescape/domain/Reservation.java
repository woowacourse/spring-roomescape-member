package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(String name, LocalDate date, LocalTime time) {
        this(null, new Name(name), date, new ReservationTime(time));
    }

    public Reservation(String name, LocalDate date, ReservationTime time) {
        this(null, new Name(name), date, time);
    }

    public Reservation(Long id, Name name, LocalDate date, ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public boolean hasId(long id) {
        return this.id == id;
    }

    public long getId() {
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
}
