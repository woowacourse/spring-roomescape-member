package roomescape.reservation.domain;

public class Reservation {
    private final Long id;
    private final String name;
    private final String date;
    private final ReservationTime time;

    public Reservation(String name, String date, ReservationTime time) {
        this(null, name, date, time);
    }

    private Reservation(Long id, String name, String date, ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation withId(Long id) {
        return new Reservation(id, this.name, this.date, this.time);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }
}