package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalTime;

public record ReservationTimeRequestDto(@JsonProperty(value = "startAt") LocalTime startAt) {
    public ReservationTimeRequestDto (LocalTime startAt){
        if (startAt == null)
            throw new IllegalArgumentException("startAt cannot be null");
        this.startAt = startAt;
    }
}
