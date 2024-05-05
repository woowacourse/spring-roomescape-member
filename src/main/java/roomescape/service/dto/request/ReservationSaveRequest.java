package roomescape.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public record ReservationSaveRequest(@NotBlank String name,
                                     @NotNull LocalDate date,
                                     @NotNull Long timeId,
                                     @NotNull Long themeId) {

    public static Reservation toEntity(ReservationSaveRequest request, ReservationTime reservationTime, Theme theme) {
        return new Reservation(request.name(), request.date(), reservationTime, theme);
    }
}
