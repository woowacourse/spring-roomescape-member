package roomescape.dto.response;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        @JsonProperty("time") ReservationTime reservationTime
) {

    public static ReservationResponse of(Reservation reservation, ReservationTime reservationTime) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                reservationTime
        );
    }
}
