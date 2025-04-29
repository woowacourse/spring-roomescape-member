package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.reservationTime.domain.ReservationTime;

public record ReservationResponse(Long id, String name, LocalDate date, ReservationTime time) {
}
