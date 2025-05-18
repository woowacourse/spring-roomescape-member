package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;
    private final Member member;

    public Reservation(Long id, LocalDate date, ReservationTime time, Theme theme,
        Member member) {
        this.id = id;
        this.date = Objects.requireNonNull(date, "[ERROR] 날짜는 null이 될 수 없습니다.");
        this.time = Objects.requireNonNull(time, "[ERROR] 시간은 null이 될 수 없습니다.");
        this.theme = Objects.requireNonNull(theme, "[ERROR] 테마는 null이 될 수 없습니다.");
        this.member = Objects.requireNonNull(member, "[ERROR] 맴버는 null이 될 수 없습니다.");
    }

    public Reservation(LocalDate date, ReservationTime time, Theme theme,
        Member member) {
        this(null, date, time, theme, member);
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
