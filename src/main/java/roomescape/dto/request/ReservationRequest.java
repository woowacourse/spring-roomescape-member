package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

public record ReservationRequest(
        @NotBlank
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @NotNull
        Long timeId,
        @NotNull
        Long themeId)
{
    public Reservation toReservation(ReservationTime reservationTime, RoomTheme roomTheme) {
        return new Reservation(new Name(name), date, reservationTime, roomTheme);
    }
}
