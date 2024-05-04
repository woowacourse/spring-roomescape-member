package roomescape.service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

public record ReservationRequest(
        @NotBlank
        String name,
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @NotNull
        Long timeId,
        @NotNull
        Long themeId)
{
    public Reservation toReservation(ReservationTime reservationTime, RoomTheme roomTheme) {
        return new Reservation(name, date, reservationTime, roomTheme);
    }
}
