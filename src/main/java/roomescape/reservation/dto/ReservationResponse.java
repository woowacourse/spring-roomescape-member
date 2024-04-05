package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.Time.domain.Time;

public record ReservationResponse(long id, String name, LocalDate date, Time time) {
}
