package roomescape.domain;

import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(String name, LocalDate date, ReservationTime time) {
        this(null, name, date, time);
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        validate(name, date, time);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation withId(Long id) {
        validateId(id);

        if (this.id != null) {
            throw new DomainException(ErrorCode.RESERVATION_ALREADY_HAS_ID);
        }

        return new Reservation(id, name, date, time);
    }

    private void validate(String name, LocalDate date, ReservationTime time) {
        validateName(name);
        validateDate(date);
        validateTime(time);
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_ID);
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_NAME);
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_DATE);
        }
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_TIME);
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
}

