package roomescape.dto.request;

import java.time.LocalDate;

import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationAddRequest(Name name, LocalDate date, Long timeId) {
}
