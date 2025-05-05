package roomescape.domain.vo;

import java.time.LocalDate;
import lombok.Builder;
import roomescape.domain.entity.ReservationTheme;
import roomescape.domain.entity.ReservationTime;

@Builder
public record ReservationDetails(
        String name,
        LocalDate date,
        ReservationTime reservationTime,
        ReservationTheme reservationTheme
) {

}
