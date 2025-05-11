package roomescape.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.user.domain.User;


public record ReservationRequest(
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @NotNull Long timeId,
        @NotNull Long themeId
) {
    public Reservation toEntityWithReservationTime(ReservationTime reservationTime, Theme theme, User user) {
        return new Reservation(
                null,
                user,
                date,
                reservationTime,
                theme
        );
    }
}
