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

    public Reservation(final Long id, final LocalDate date, final ReservationTime time, final Theme theme, final Member member) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public Reservation(final Long id, final String date, final long timeId, final long themeId, final long memberId) {
        this(
                id,
                validateDateFormatAndReturn(date),
                new ReservationTime(timeId),
                new Theme(themeId),
                new Member(memberId)
        );

    }

    private static LocalDate validateDateFormatAndReturn(final String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException exception) {
            throw new InvalidDateException("날짜 형식이 올바르지 않습니다.");
        }
    }

    public Reservation assignId(final Long id) {
        return new Reservation(id, date, time, theme, member);
    }

    public Reservation assignTime(final ReservationTime time) {
        return new Reservation(id, date, time, theme, member);
    }

    public Reservation assignTheme(final Theme theme) {
        return new Reservation(id, date, time, theme, member);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(date, that.date) &&
                Objects.equals(time, that.time) &&
                Objects.equals(theme, that.theme);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(date);
        result = 31 * result + Objects.hashCode(time);
        result = 31 * result + Objects.hashCode(theme);
        return result;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", name=" + name +
                ", date=" + date +
                ", time=" + time +
                ", theme=" + theme +
                '}';
    }
}
