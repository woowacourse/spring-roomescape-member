package roomescape.domain;

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
    private final LocalDateTime createdAt;

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme, LocalDateTime createdAt) {
        this(null, name, date, time, theme, createdAt);
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme,
                       LocalDateTime createdAt) {
        validateCreatedAtAfterReservationTime(date, time.getStartAt(), createdAt);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.createdAt = createdAt;
    }

    private static void validateCreatedAtAfterReservationTime(LocalDate date, LocalTime time,
                                                              LocalDateTime createdAt) {
        LocalDateTime reservationDateTime = date.atTime(time);
        if (reservationDateTime.isBefore(createdAt)) {
            throw new IllegalArgumentException("지나간 시간에 대한 예약 생성은 불가능하다.");
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(date, that.date) && Objects.equals(time, that.time)
                && Objects.equals(theme, that.theme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, time, theme);
    }
}
