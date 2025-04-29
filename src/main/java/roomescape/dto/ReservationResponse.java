package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationResponse(
    Long id,
    String name,
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
    ReservationTime time) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getName(),
            reservation.getDate(),
            reservation.getTime());
    }
}
