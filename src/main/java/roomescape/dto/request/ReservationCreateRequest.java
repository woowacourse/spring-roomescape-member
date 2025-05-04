package roomescape.dto.request;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationCreateRequest(
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        Long timeId,
        Long themeId
) {
    public Reservation toReservation(ReservationTime time, Theme theme) {
        return new Reservation(null, name, date, time, theme);
    }
}
