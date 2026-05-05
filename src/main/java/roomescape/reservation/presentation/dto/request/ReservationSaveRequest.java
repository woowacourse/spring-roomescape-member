package roomescape.reservation.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

import java.time.LocalDate;

public record ReservationSaveRequest(@NotBlank String name,
                                     @JsonFormat(pattern = "yyyy-MM-dd") @NotNull LocalDate date,
                                     @NotNull Long timeId,
                                     @NotNull Long themeId) {
    public Reservation toDomain(ReservationTime time, Theme theme) {
        return new Reservation(
                null,
                name,
                date,
                time,
                theme);
    }
}
