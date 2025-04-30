package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.ReservationTimeResponse;
import roomescape.theme.domain.Theme;

public record ReservationResponse(Long id, String name, Theme theme, @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                  ReservationTimeResponse time) {
}
