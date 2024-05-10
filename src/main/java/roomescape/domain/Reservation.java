package roomescape.domain;

public class Reservation {

    private final Long id;
    private final String date;
    private final ReservationTime time;
    private final ReservationTheme theme;
    private final Member member;

    public Reservation(Long id, String date, ReservationTime time, ReservationTheme theme, Member member) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public ReservationTheme getTheme() {
        return theme;
    }

    public Member getMember() {
        return member;
    }
}
