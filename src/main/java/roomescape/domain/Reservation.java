package roomescape.domain;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this(null, name, date, reservationTime, theme);
    }

    public Reservation copyWithId(Long id) {
        return new Reservation(id, name, date, reservationTime, theme);
    }

    public void validatePastDateTime() {
        LocalDate now = LocalDate.now();
        if (date.isBefore(now) || (date.equals(now) && reservationTime.isPastTime())) {
            throw new IllegalArgumentException("지난 날짜와 시간의 예약은 생성 불가능합니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 비어있을 수 없습니다.");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("이름의 최대 제한 길이를 초과했습니다.");
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

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
