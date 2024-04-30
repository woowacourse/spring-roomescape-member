package roomescape.web.dto;

import java.time.DateTimeException;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.InvalidRequestReservationException;

public class ReservationRequest {
    private final LocalDate date;
    private final String name;
    private final Long timeId;

    public ReservationRequest(String date, String name, String timeId) {
        validate(date, name, timeId);
        this.date = LocalDate.parse(date);
        this.name = name;
        this.timeId = Long.parseLong(timeId);
    }

    public Reservation toReservation(ReservationTime reservationTime) {
        return new Reservation(name, date, reservationTime);
    }

    public void validate(String date, String name, String timeId) {
        if (date == null || name.isBlank() || timeId == null) {
            throw new InvalidRequestReservationException();
        }
        try {
            LocalDate.parse(date);
        } catch (DateTimeException e) {
            throw new InvalidRequestReservationException();
        }
    }

    public LocalDate date() {
        return date;
    }

    public String name() {
        return name;
    }

    public Long timeId() {
        return timeId;
    }
}
