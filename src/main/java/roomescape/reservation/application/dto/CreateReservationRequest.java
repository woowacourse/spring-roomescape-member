package roomescape.reservation.application.dto;

import roomescape.member.domain.Member;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public class CreateReservationRequest {
    private final Member member;
    private final Theme theme;
    private final ReservationDate date;
    private final ReservationTime time;

    public CreateReservationRequest(Member member, Theme theme, ReservationDate date, ReservationTime time) {
        this.member = member;
        this.theme = theme;
        this.date = date;
        this.time = time;
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
