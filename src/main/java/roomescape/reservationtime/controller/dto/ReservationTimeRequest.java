package roomescape.reservationtime.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ReservationTimeRequest(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date
) {
}
