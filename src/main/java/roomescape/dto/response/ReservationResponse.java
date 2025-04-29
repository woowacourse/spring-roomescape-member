package roomescape.dto.response;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        @JsonProperty("time") ReservationTimeResponse reservationTimeResponse
) {

    public static ReservationResponse of(Reservation reservation, ReservationTimeResponse reservationTimeResponse) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                reservationTimeResponse
        );
    }
}
