package roomescape.domain;

public class Reservation {
    private final long id;
    private final MemberName name;
    private final Schedule schedule;
    private final Theme theme;
    private final Member member;

    public Reservation(long id, MemberName name, Schedule schedule, Theme theme, Member member) {
        this.id = id;
        this.name = name;
        this.schedule = schedule;
        this.theme = theme;
        this.member = member;
    }

    public Reservation(final long id, final Reservation reservation) {
        this(id, reservation.name, reservation.schedule, reservation.theme, reservation.member);
    }

    public Reservation(final long id, final String name, final String date, final ReservationTime reservationTime,
                       final Theme theme, final Member member) {
        this(id, new MemberName(name), new Schedule(new ReservationDate(date), reservationTime), theme, member);
    }

    public Reservation(final String date, final Theme theme, final ReservationTime reservationTime,
                       final Member member) {
        this(member.getId(), new MemberName(member.getName()),
                new Schedule(new ReservationDate(date), reservationTime), theme, member);
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

    public Member getLoginMember() {
        return member;
    }
}
