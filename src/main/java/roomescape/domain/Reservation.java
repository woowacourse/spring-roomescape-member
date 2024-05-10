package roomescape.domain;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class Reservation {
    private final Long id;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;
    private final Member member;

    public Reservation(Long id, LocalDate date, ReservationTime time, Theme theme, Member member) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public Reservation(LocalDate date, ReservationTime time, Theme theme, Member member) {
        this(null, date, time, theme, member);
    }
}
