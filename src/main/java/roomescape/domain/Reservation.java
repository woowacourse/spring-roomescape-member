package roomescape.domain;

public class Reservation {
    private static final long NO_ID = 0;

    private final long id;
    private final MemberName name;
    private final Schedule schedule;
    private final Theme theme;

    public Reservation(final long id, final MemberName name, final Schedule schedule, final Theme theme) {
        this.id = id;
        this.name = name;
        this.schedule = schedule;
        this.theme = theme;
    }

    public Reservation(final long id, final Reservation reservation) {
        this(id, reservation.name, reservation.schedule, reservation.theme);
    }

    public Reservation(final long id, final String name, final String date, final ReservationTime reservationTime,
                       final Theme theme) {
        this(id, new MemberName(name), new Schedule(new ReservationDate(date), reservationTime), theme);
    }

    public Reservation(final String name, final String date, final ReservationTime reservationTime, final Theme theme) {
        this(NO_ID, new MemberName(name), new Schedule(new ReservationDate(date), reservationTime), theme);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getDate() {
        return schedule.getDate();
    }

    public String getTime() {
        return schedule.getTime();
    }

    public ReservationTime getReservationTime() {
        return schedule.getReservationTime();
    }

    public Theme getTheme() {
        return theme;
    }
}
