package roomescape.dto.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;
import roomescape.dto.time.TimeResponse;

public record ReservationResponse(Long id, String name, LocalDate date,
                                  @JsonProperty("time") TimeResponse timeResponse) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                TimeResponse.from(reservation.getTime())
        );
    }
}
