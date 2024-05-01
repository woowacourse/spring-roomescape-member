package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {

    private Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        validateDateIsFuture(toLocalDateTime(date, reservationTime));
        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this(null, name, date, reservationTime, theme);
    }

    private void validateDateIsFuture(LocalDateTime localDateTime) {
        if (localDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
        }
    }

    public LocalDateTime toLocalDateTime(LocalDate date, ReservationTime reservationTime) {
        return LocalDateTime.of(date, reservationTime.getStartAt());
    }

    public boolean isSameReservation(Long id) {
        return this.id.equals(id);
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

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
