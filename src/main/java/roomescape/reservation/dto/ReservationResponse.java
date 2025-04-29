package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.dto.ReservationTimeResponse;

public record ReservationResponse(Long id, String name, LocalDate date, ReservationTimeResponse time) {
    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                new ReservationTimeResponse(
                        reservation.getTime().getId(),
                        reservation.getTime().getStartAt()
                )
        );
    }
}
