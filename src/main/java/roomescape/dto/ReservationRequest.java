package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationRequest(String name, String date, Long timeId) {
    public Reservation toDomain(ReservationTime reservationTime) {
        return new Reservation(new Name(name), LocalDate.parse(date), reservationTime);
    }
}
