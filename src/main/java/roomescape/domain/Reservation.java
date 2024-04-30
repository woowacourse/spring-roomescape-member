package roomescape.domain;

import java.time.LocalDate;

public class Reservation {

    private Long id;
    private Name name;
    private LocalDate date;
    private ReservationTime time;

    public Reservation() {
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime reservationTime) {
        this.id = id;
        this.name = new Name(name);
        this.date = date;
        this.time = reservationTime;
    }

    public Reservation(String name, LocalDate date, ReservationTime reservationTime) {
        this(null, name, date, reservationTime);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }
}
