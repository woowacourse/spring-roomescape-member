package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

public record ReservationRequest(
        String name,
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        Long timeId,
        Long themeId
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
