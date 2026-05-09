package roomescape.domain.reservation;

import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {

    private Long id;
    private String name;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;
    private LocalDateTime createdAt;

    public Reservation() {
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme, LocalDateTime createdAt) {
        validateFutureDate(createdAt);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.createdAt = createdAt;
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

    private static void validateFutureDate(LocalDateTime futureDate) {
        if (futureDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
        }
    }
}
