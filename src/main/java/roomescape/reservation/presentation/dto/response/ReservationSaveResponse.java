package roomescape.reservation.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.presentation.dto.response.dto.TimeInformation;

public record ReservationSaveResponse(@NotNull Long id,
                                      @NotNull Long themeId,
                                      @NotNull String name,
                                      @JsonFormat(pattern = "yyyy-MM-dd") @NotNull LocalDate date,
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
