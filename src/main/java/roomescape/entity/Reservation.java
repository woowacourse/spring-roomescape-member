package roomescape.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final LocalDate date;
    private final Member member;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, LocalDate date, Member member, ReservationTime time, Theme theme) {
        this.id = id;
        this.date = Objects.requireNonNull(date);
        this.member = Objects.requireNonNull(member);
        this.time = Objects.requireNonNull(time);
        this.theme = Objects.requireNonNull(theme);
    }

    public Reservation(LocalDate date, Member member, ReservationTime time, Theme theme) {
        this(null, date, member, time, theme);
    }

    public Reservation copyWithId(Long id) {
        return new Reservation(id, date, member, time, theme);
    }

    public void validatePastDateTime() {
        LocalDate now = LocalDate.now();
        if (date.isBefore(now) || (date.equals(now) && time.isPastTime())) {
            throw new IllegalArgumentException("지난 날짜와 시간의 예약은 생성 불가능합니다.");
        }
    }

    public Long getId() {
        if (id == null) {
            throw new NullPointerException("id값이 존재하지 않습니다.");
        }
        return id;
    }

    public Member getMember() {
        return member;
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
}
