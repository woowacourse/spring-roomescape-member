package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeRequestDto(@JsonProperty(value = "startAt") @NotNull LocalTime startAt) {

}
