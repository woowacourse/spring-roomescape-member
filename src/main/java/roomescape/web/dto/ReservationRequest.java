package roomescape.web.dto;

import java.time.DateTimeException;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

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

    public void validate(String date, String name, String timeId) {
        if (date == null || name.isBlank() || timeId == null) {
            throw new IllegalArgumentException();
        }
        try {
            LocalDate.parse(date);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException();
        }
    }

    public Reservation toReservation(ReservationTime reservationTime) {
        return new Reservation(name, date, reservationTime);
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public Long getTimeId() {
        return timeId;
    }
}
