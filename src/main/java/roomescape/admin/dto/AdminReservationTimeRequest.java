package roomescape.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record AdminReservationTimeRequest(
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {
}
