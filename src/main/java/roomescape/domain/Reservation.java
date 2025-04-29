package roomescape.domain;

import java.time.LocalDate;
import roomescape.exception.reservation.ReservationFieldRequiredException;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    private Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        validate(name, date, time);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation(String name, LocalDate date, ReservationTime time) {
        this(null, name, date, time);
    }

    public Reservation withId(Long id) {
        return new Reservation(id, this.name, this.date, this.time);
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

    private void validate(String name, LocalDate date, ReservationTime time) {
        validateName(name);
        validateDate(date);
        validateTime(time);
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new ReservationFieldRequiredException("시간");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ReservationFieldRequiredException("이름");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new ReservationFieldRequiredException("날짜");
        }
    }
}
