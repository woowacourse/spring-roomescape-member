package roomescape.entity;

import roomescape.exception.PastReservationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public static Reservation create(String name, LocalDate date, ReservationTime time, Theme theme) {
        // TODO: 운영시간 검증 구현 필요
        validatePastDateTime(date, time);
        return new Reservation(null, name, date, time, theme);
    }

    private static void validatePastDateTime(LocalDate date, ReservationTime time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        if (reservationDateTime.isBefore(now)) {
            throw new PastReservationException("현재보다 과거의 날짜로 예약 할 수 없습니다.");
        }
    }

    public boolean isSameId(long id) {
        return this.id == id;
    }

    public boolean isBooked(ReservationTime reservationTime, Theme theme) {
        LocalTime startAt = time.getStartAt();
        return reservationTime.hasConflict(theme, startAt);
    }

    public long getId() {
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
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(date, that.date) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, time);
    }
}
