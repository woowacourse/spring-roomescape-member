package roomescape.domain;

public class Reservation {
    private static final long NO_ID = 0;

    private final long id;
    private final MemberName name;
    private final Schedule schedule;
    private final Theme theme;
    private final LoginMember loginMember;

    public Reservation(long id, MemberName name, Schedule schedule, Theme theme, LoginMember loginMember) {
        this.id = id;
        this.name = name;
        this.schedule = schedule;
        this.theme = theme;
        this.loginMember = loginMember;
    }

    public Reservation(final long id, final Reservation reservation) {
        this(id, reservation.name, reservation.schedule, reservation.theme, reservation.loginMember);
    }

    public Reservation(final long id, final String name, final String date, final ReservationTime reservationTime,
                       final Theme theme, final LoginMember loginMember) {
        this(id, new MemberName(name), new Schedule(new ReservationDate(date), reservationTime), theme, loginMember);
    }

    public Reservation(final String date, final Theme theme, final ReservationTime reservationTime,
                       final LoginMember loginMember) {
        this(loginMember.getId(), new MemberName(loginMember.getName()),
                new Schedule(new ReservationDate(date), reservationTime), theme, loginMember);
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

    public Schedule getSchedule() {
        return schedule;
    }

    public Theme getTheme() {
        return theme;
    }

    public LoginMember getLoginMember() {
        return loginMember;
    }
}
