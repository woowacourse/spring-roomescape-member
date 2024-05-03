package roomescape.dto.reservation;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;

public record ReservationRequest(
        @NonNull String name,
        @NonNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @NonNull Long timeId,
        @NonNull Long themeId
) {

    public Reservation toReservation(Time time, Theme theme) {
        return new Reservation(this.name, this.date, time, theme);
    }
}
