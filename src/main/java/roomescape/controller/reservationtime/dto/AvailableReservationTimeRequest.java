package roomescape.controller.reservationtime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record AvailableReservationTimeRequest(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date
) {
}
