package roomescape.reservation.domain;

import roomescape.exception.InvalidDateException;
import roomescape.member.domain.Member;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final Member member;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(final Long id, final Member member, final LocalDate date, final ReservationTime time, final Theme theme) {
        validateDateExist(date);
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public boolean isPast(LocalDate currentDate) {
        return date.isBefore(currentDate);
    }

    public boolean isDate(LocalDate currentDate) {
        return date.equals(currentDate);
    }

    private void validateDateExist(final LocalDate date) {
        if (Objects.isNull(date)) {
            throw new InvalidDateException("날짜가 비어있습니다.");
        }
    }

    public Long getId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(member, that.member) && Objects.equals(date, that.date) && Objects.equals(time, that.time) && Objects.equals(theme, that.theme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, date, time, theme);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", member=" + member +
                ", date=" + date +
                ", time=" + time +
                ", theme=" + theme +
                '}';
    }
}
