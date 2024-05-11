package roomescape.reservation.domain;

import java.time.LocalDate;
import roomescape.member.domain.MemberInfo;

public class Reservation {

    private final Long id;
    private final LocalDate date;
    private final ReservationTime time;
    private final ReservationTheme theme;
    private final MemberInfo member;

    public Reservation(Long id, LocalDate date, ReservationTime time, ReservationTheme theme,
                       MemberInfo member) {
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

    public ReservationTime getTime() {
        return time;
    }

    public ReservationTheme getTheme() {
        return theme;
    }

    public MemberInfo getMember() {
        return member;
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }

    public Long getMemberId() {
        return member.getId();
    }
}
