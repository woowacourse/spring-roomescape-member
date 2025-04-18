package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public record Reservation(long id, Person person, ReservationTime reservationTime) {

    public String getPersonName() {
        return person.name();
    }

    public LocalDate getDate() {
        return reservationTime.getDate();
    }

    public LocalTime getTime() {
        return reservationTime.getTime();
    }
}
