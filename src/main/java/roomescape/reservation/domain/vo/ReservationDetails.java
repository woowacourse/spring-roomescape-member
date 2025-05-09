package roomescape.reservation.domain.vo;

import java.time.LocalDate;
import lombok.Builder;
import roomescape.reservation.domain.entity.ReservationTheme;
import roomescape.reservation.domain.entity.ReservationTime;

@Builder
public record ReservationDetails(
        String name,
        LocalDate date,
        ReservationTime reservationTime,
        ReservationTheme reservationTheme
) {

}
