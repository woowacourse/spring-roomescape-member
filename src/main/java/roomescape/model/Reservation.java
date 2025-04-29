package roomescape.model;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime reservationTime;

    public Reservation(Long id, String name, LocalDate date, ReservationTime reservationTime) {
        this.id = id;
        validateName(name);
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
    }

    public void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("사용자의 이름이 NULL일 수 없습니다.");
        }
        if (name.isEmpty() || name.length() > 9) {
            throw new IllegalArgumentException("사용자의 이름은 1자에서 10자 이내여야 합니다.");
        }
    }

    public boolean isPast(LocalDate other) {
        return date.isBefore(other);
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
}
