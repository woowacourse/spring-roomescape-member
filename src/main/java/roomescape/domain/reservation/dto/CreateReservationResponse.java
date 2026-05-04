package roomescape.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.domain.reservation.Reservation;

public record CreateReservationResponse(
    Long id,
    String name,
    LocalDate date,
    @JsonFormat(pattern = "HH:mm")
    LocalTime time
) {

    public static CreateReservationResponse from(Reservation reservation) {
        return new CreateReservationResponse(
            reservation.getId(),
            reservation.getName(),
            reservation.getDate(),
            reservation.getTime().getStartAt()
        );
    }
}
