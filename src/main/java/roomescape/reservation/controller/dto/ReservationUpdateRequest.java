package roomescape.reservation.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ReservationUpdateRequest(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        Long timeId
) {
}
