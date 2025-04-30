package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;


public record ReservationRequest(
        @NotBlank String name,
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @NotNull Long timeId,
        @NotNull Long themeId
) {
    public Reservation toEntityWithReservationTime(ReservationTime reservationTime, Theme theme) {
        return new Reservation(
                null,
                name,
                date,
                reservationTime,
                theme
        );
    }
}
