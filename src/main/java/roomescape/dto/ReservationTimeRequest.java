package roomescape.dto;

import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public record ReservationTimeRequest (
        @JsonFormat(pattern = "HH:mm") LocalTime startAt
){
}
