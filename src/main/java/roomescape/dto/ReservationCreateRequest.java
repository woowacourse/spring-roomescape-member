package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public record ReservationCreateRequest(String name,
                                       @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                       Long timeId,
                                       Long themeId) {
    public Reservation createReservation(ReservationTime time, Theme theme) {
        return new Reservation(name, date, time, theme);
    }
}
