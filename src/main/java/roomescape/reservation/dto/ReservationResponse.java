package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.time.dto.TimeResponse;

public record ReservationResponse(Long id, String name, LocalDate date, TimeResponse time) {
}
