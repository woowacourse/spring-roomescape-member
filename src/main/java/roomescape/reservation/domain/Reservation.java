package roomescape.reservation.domain;

import roomescape.exception.InvalidDateException;
import roomescape.exception.InvalidNameException;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Reservation {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(final Long id, final String name, final String date, final ReservationTime time, final Theme theme) {
        validateNameExist(name);
        validateDateExist(date);
        this.id = id;
        this.name = name;
        this.date = LocalDate.parse(date, DATE_FORMAT);
        this.time = time;
        this.theme = theme;
    }

    private void validateNameExist(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new InvalidNameException("예약자명이 비어있습니다.");
        }
    }

    private void validateDateExist(final String date) {
        if (Objects.isNull(date) || date.isBlank()) {
            throw new InvalidDateException("날짜가 비어있습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(date, that.date) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, time);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", theme=" + theme +
                '}';
    }
}
