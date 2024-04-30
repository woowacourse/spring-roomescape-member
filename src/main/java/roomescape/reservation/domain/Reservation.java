package roomescape.reservation.domain;

import java.time.LocalDate;
import roomescape.reservation.handler.exception.CustomException;
import roomescape.reservation.handler.exception.ExceptionCode;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        validateName(name);

        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public void validateName(String name) {
        if (name.isEmpty() || name.length() > 10) {
            throw new CustomException(ExceptionCode.INVALID_NAME_LENGTH);
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

    public Long getTimeId() {
        return time.getId();
    }
}
