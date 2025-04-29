package roomescape.business;

import java.time.LocalDate;

public class Reservation {

    private Long id;
    private final String name;
    private final LocalDate date;
    private ReservationTime time;

    public Reservation(String name, LocalDate date, ReservationTime time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
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

    public void setId(Long id) {
        this.id = id;
    }
}
