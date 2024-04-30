package roomescape.domain;

import java.time.LocalDate;

public class Reservation {

    private final Long id;
    private final UserName name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(Long id, UserName name, LocalDate date, ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation(UserName name, LocalDate date, ReservationTime time) {
        this.id = null;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public UserName getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getReservationTime() {
        return time;
    }
}
