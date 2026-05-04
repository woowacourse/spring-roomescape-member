package roomescape.domain;

public class Reservation {
    private final long id;
    private final Name name;
    private final ReservationDate date;
    private final ReservationTime time;

    private Reservation(long id, Name name, ReservationDate date, ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static Reservation of(long id, String name, String date, ReservationTime time) {
        return new Reservation(id, Name.from(name), ReservationDate.from(date), time);
    }

    public static Reservation of(long id, String name, ReservationDate date, ReservationTime time) {
        return new Reservation(id, Name.from(name), date, time);
    }

    public static Reservation of(String name, String date, ReservationTime time) {
        return new Reservation(0L, Name.from(name), ReservationDate.from(date), time);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public ReservationDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }
}
