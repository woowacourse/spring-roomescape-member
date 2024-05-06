package roomescape.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;

import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank String name,
        @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @NotNull @Positive Long timeId,
        @NotNull @Positive Long themeId
) {

    public Reservation toReservation(Time time, Theme theme) {
        return new Reservation(this.name, this.date, time, theme);
    }
}
