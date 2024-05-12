package roomescape.domain.reservation;

import roomescape.domain.member.Member;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;

import java.time.LocalDate;

public class Reservation {

    private final Long id;
    private final LocalDate date;
    private final Time time;
    private final Theme theme;
    private final Member member;

    public Reservation(LocalDate date, Time time, Theme theme, Member member) {
        this(null, date, time, theme, member);
    }

    public Reservation(Long id, LocalDate date, Time time, Theme theme, Member member) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Member getMember() {
        return member;
    }
}
