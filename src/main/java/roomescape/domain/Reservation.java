package roomescape.domain;

public class Reservation {
    private final long id;
    private final Name name;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(long id, Name name, ReservationDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation of(long id, String name, String date, ReservationTime time, Theme theme) {
        return new Reservation(id, Name.from(name), ReservationDate.from(date), time, theme);
    }

    public static Reservation of(long id, String name, ReservationDate date, ReservationTime time, Theme theme) {
        return new Reservation(id, Name.from(name), date, time, theme);
    }

    public static Reservation of(String name, String date, ReservationTime time, Theme theme) {
        return new Reservation(0L, Name.from(name), ReservationDate.from(date), time, theme);
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

    public Theme getTheme() {
        return theme;
    }
}
