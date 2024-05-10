package roomescape.reservation.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.reservation.domain.Reservation;

import java.time.LocalDate;

public record ReservationResponse(
        long id,
        String name,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme) {
    public ReservationResponse(Reservation reservation) {
        this(reservation.getId(), reservation.getName(), reservation.getDate(),
                new ReservationTimeResponse(reservation.getReservationTime()),
                new ThemeResponse(reservation.getTheme()));
    }
}
