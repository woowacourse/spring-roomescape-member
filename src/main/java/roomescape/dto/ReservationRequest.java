package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationRequest(
        @NotNull @NotBlank String name,
        @NotNull @NotBlank String date,
        @NotNull @Positive Long timeId,
        @NotNull @Positive Long themeId) {
    public Reservation toDomain(ReservationTime reservationTime, Theme theme) {
        return new Reservation(new Name(name), LocalDate.parse(date), reservationTime, theme);
    }
}
