package roomescape.domain;

import java.time.LocalDate;
import roomescape.exception.InvalidInputException;

public class Reservation {

    private final Long id;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;
    private final Member member;

    public Reservation(Long id, LocalDate date, ReservationTime time, Theme theme, Member member) {
        validate(date);
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public Reservation(LocalDate date, ReservationTime time, Theme theme, Member member) {
        this(null, date, time, theme, member);
    }

    private void validate(LocalDate date) {
        if (date == null) {
            throw new InvalidInputException("예약 날짜가 입력되지 않았습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getReservationTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Member getMember() {
        return member;
    }
}
