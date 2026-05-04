package roomescape.reservation.presentation.dto.response;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.presentation.dto.response.dto.TimeInformation;

public record ReservationSaveResponse(@NotNull Long id,
                                      @NotNull Long themeId,
                                      @NotNull String name,
                                      @NotNull LocalDate date,
                                      @NotNull TimeInformation time) {
    public static ReservationSaveResponse of(ReservationTime time, Theme theme, Reservation reservation) {
        return new ReservationSaveResponse(
                reservation.getId(),
                theme.getId(),
                reservation.getName(),
                reservation.getDate(),
                new TimeInformation(time.getId(), time.getStartAt())
        );
    }
}
