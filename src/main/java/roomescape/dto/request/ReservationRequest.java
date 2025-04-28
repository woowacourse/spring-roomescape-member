package roomescape.dto.request;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.Reservation;

public record ReservationRequest(
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        Long timeId
) {
    public Reservation toReservation() {
        return new Reservation(null, this.name, this.date, this.timeId);
    }
}
