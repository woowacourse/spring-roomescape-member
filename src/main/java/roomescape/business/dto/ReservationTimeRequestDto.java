package roomescape.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalTime;

public record ReservationTimeRequestDto(@JsonProperty("startAt") LocalTime startAt) {
}
