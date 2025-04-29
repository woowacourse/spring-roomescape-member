package roomescape.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this.id = Objects.requireNonNull(id);
        this.name = validateNonBlank(name);
        this.date = Objects.requireNonNull(date);
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this.id = null;
        this.name = validateNonBlank(name);
        this.date = Objects.requireNonNull(date);
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public void validateReservationDateInFuture() {
        if (!this.date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("과거 및 당일 예약은 불가능합니다.");
        }
    }

    private String validateNonBlank(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 null이거나 공백일 수 없습니다");
        }
        return name;
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

    public LocalTime getTime() {
        return reservationTime.getStartAt();
    }

    public Long getTimeId() {
        return reservationTime.getId();
    }
}
