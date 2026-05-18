package roomescape.reservationTime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record AdminReservationTimeRequest(
        @JsonFormat(pattern = "HH:mm")
        @NotNull
        LocalTime startAt
) {
}
