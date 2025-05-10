package roomescape.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.entity.ReservationTime;

public class ReservationRequest {

    public record ReservationCreateRequest(
            @NotNull LocalDate date,
            @NotNull String name,
            @NotNull Long timeId,
            @NotNull Long themeId
    ) {
        public Reservation toEntity(ReservationTime reservationTime) {
            return new Reservation(0L, name, date, reservationTime, themeId);
        }
    }
}
