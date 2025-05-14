package roomescape.reservation.domain;

import roomescape.member.domain.Member;
import roomescape.reservation.exception.ReservationFieldRequiredException;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public class Reservation {
    private final Long id;
    private final Member member;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(Long id, Member member, ReservationDate date, ReservationTime time, Theme theme) {
        validate(member, date, time, theme);
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation createWithoutId(Member member, ReservationDate date, ReservationTime time, Theme theme) {
        return new Reservation(null, member, date, time, theme);
    }

    public static Reservation createWithId(Long id, Member member, ReservationDate date, ReservationTime time,
                                           Theme theme) {
        return new Reservation(id, member, date, time, theme);
    }

    private void validate(Member member, ReservationDate date, ReservationTime time, Theme theme) {
        validateMember(member);
        validateDate(date);
        validateTime(time);
        validateTheme(theme);
    }

    private void validateMember(Member member) {
        if (member == null) {
            throw new ReservationFieldRequiredException();
        }
    }

    private void validateDate(ReservationDate date) {
        if (date == null) {
            throw new ReservationFieldRequiredException();
        }
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new ReservationFieldRequiredException();
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new ReservationFieldRequiredException();
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
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
