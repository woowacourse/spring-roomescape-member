package roomescape.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationtime.ReservationTime;

public record ReservationResponse(
    Long id,
    String name,
    LocalDate date,
    ReservationTimePayload time
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getName(),
            reservation.getDate().getDate(),
            ReservationTimePayload.from(reservation.getTime())
        );
    }

    public record ReservationTimePayload(
        Long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
    ) {

        public static ReservationTimePayload from(ReservationTime reservationTime) {
            return new ReservationTimePayload(reservationTime.getId(), reservationTime.getStartAt());
        }
    }
}
