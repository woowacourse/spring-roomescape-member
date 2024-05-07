package roomescape.reservation.domain;

import java.time.LocalDate;

public class Reservation {

    private Long id;
    private final Name name;
    private final LocalDate date;
    private final Theme theme;
    private final ReservationTime reservationTime;

    public Reservation(Name name, LocalDate date, Theme theme, ReservationTime reservationTime) {
        validateDate(date);
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.reservationTime = reservationTime;
    }

    public Reservation(Long id, Name name, LocalDate date, Theme theme, ReservationTime reservationTime) {
        validateDate(date);
        this.id = id;
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.reservationTime = reservationTime;
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("날짜 값이 입력되지 않았습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return reservationTime;
    }
}
