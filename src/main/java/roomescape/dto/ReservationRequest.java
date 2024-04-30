package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationRequest(
        @NotNull @NotBlank String name,
        @NotNull @NotBlank String date,
        @NotNull @Positive Long timeId) {
    public Reservation toDomain(ReservationTime reservationTime) {
        return new Reservation(new Name(name), LocalDate.parse(date), reservationTime);
    }
}
