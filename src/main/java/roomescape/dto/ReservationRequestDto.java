package roomescape.dto;

import java.time.LocalDate;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

public record ReservationRequestDto(
        String name,
        LocalDate date,
        Long timeId
) {
    public Reservation convertToReservation() {
        return new Reservation(
                this.name,
                this.date,
                new ReservationTime(timeId)
        );
    }
}
