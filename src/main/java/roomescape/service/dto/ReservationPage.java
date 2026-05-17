package roomescape.service.dto;

import java.util.List;
import roomescape.domain.Reservation;

public record ReservationPage(List<Reservation> reservations, long totalCount) {
}