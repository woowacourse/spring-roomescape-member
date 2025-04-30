package roomescape.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(String name, LocalDateTime currentDateTime, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);

        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.startAt()); //TODO: id를 처음부터 가질 경우 고려하기
        if (reservationDateTime.isBefore(currentDateTime)) {
            throw new IllegalArgumentException("지난 날짜와 시간에 대한 예약은 불가능합니다.");
        }
        Duration duration = Duration.between(currentDateTime, reservationDateTime);
        if (duration.toMinutes() < 10) {
            throw new IllegalArgumentException("예약 시간까지 10분도 남지 않아 예약이 불가합니다.");
        }
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        if (name.length() < 2 || name.length() > 5) {
            throw new IllegalArgumentException("예약자명은 2글자에서 5글자까지만 가능합니다.");
        }
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public boolean isStartAt(ReservationTime reservationTime) {
        return time.equals(reservationTime);
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
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(date, that.date) && Objects.equals(time, that.time) && Objects.equals(theme, that.theme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, time, theme);
    }
}
