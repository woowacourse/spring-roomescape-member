package roomescape.domain;

import roomescape.domain.exception.InvalidDateException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

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

    public Reservation(Long id, String date, ReservationTime time, Theme theme, Member member) {
        this(id, validateDateAndParse(date), time, theme, member);
    }

    public Reservation(Long id, String date, long timeId, long themeId, long memberId) {
        this(id, date, new ReservationTime(timeId), new Theme(themeId), new Member(memberId));
    }

    private static LocalDate validateDateAndParse(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException exception) {
            throw new InvalidDateException("날짜 형식이 올바르지 않습니다.");
        }
    }

    public Reservation assignId(Long id) {
        return new Reservation(id, date, time, theme, member);
    }

    public Reservation assignTime(ReservationTime time) {
        return new Reservation(id, date, time, theme, member);
    }

    public Reservation assignTheme(Theme theme) {
        return new Reservation(id, date, time, theme, member);
    }

    public Reservation assignMember(Member member) {
        return new Reservation(id, date, time, theme, member);
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(date, that.date) &&
                Objects.equals(time, that.time) &&
                Objects.equals(theme, that.theme) &&
                Objects.equals(member, that.member);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(date);
        result = 31 * result + Objects.hashCode(time);
        result = 31 * result + Objects.hashCode(theme);
        result = 31 * result + Objects.hashCode(member);
        return result;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", theme=" + theme +
                ", member=" + member +
                '}';
    }
}
