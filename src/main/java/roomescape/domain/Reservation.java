package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.exception.InvalidReservationException;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        validate(name, date, time);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    private void validate(String name, LocalDate date, ReservationTime time) {
        if (name == null || name.isBlank()) {
            throw new InvalidReservationException("이름은 공백일 수 없습니다");
        }
        if (date == null || time == null) {
            throw new InvalidReservationException("시간은 공백일 수 없습니다.");
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
        return time;
    }

    public LocalTime getStartAt() {
        return time.getTime();
    }
}
