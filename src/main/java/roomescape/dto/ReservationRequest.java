package roomescape.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;

public record ReservationRequest(
        String name,
        LocalDate date,
        Long timeId
) {

    public Reservation toEntity() {
        return new Reservation(null, this.name(), this.date(), new ReservationTime(timeId, LocalTime.MIN));
    }
}
