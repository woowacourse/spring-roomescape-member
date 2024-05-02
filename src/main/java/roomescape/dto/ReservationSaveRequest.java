package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

import java.time.LocalDate;

public record ReservationSaveRequest(
        String name,
        @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate date,
        Long timeId,
        Long themeId
) {

    public Reservation toReservation(final ReservationTime reservationTime, final Theme theme) {
        return new Reservation(name, date, reservationTime, theme);
    }
}
