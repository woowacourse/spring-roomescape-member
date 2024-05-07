package roomescape.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public record ReservationRequest(String name,
                                 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                 LocalDate date, Long timeId, Long themeId) {

    public Reservation toEntity(ReservationTime reservationTime, Theme theme) {
        return new Reservation(null, name, date, reservationTime, theme);
    }
}
