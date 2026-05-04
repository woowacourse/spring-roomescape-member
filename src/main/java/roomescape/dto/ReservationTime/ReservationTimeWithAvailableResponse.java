package roomescape.dto.ReservationTime;

import java.util.List;
import roomescape.domain.ReservationTime.ReservationTimeWithAvailable;

public record ReservationTimeWithAvailableResponse(List<ReservationTimeWithAvailable> reservationTimesWithAvailable) {
}
