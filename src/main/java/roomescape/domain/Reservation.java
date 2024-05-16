package roomescape.domain;

import java.time.LocalDate;

public class Reservation {
    private Long id;
    private LocalDate date;
    private Member member;
    private ReservationTime time;
    private Theme theme;

    public Reservation() {
    }

    public Reservation(final LocalDate date, final Member member, final ReservationTime time, final Theme theme) {
        this(null, date, member, time, theme);
    }

    public Reservation(final Long id, final LocalDate date, final Member member, final ReservationTime time, final Theme theme) {
        validateReservation(date, time);
        this.id = id;
        this.date = date;
        this.member = member;
        this.time = time;
        this.theme = theme;
    }

    private void validateReservation(final LocalDate date, final ReservationTime time) {
        if (date == null || time == null) {
            throw new IllegalArgumentException("비어있는 입력이 존재할 수 없습니다.");
        }
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

    public Theme getTheme() {
        return theme;
    }

    public Member getMember() {
        return member;
    }
}
