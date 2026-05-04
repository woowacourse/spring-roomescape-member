package roomescape.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ReservationRequest(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        String name,
        Long timeId
) {
}
