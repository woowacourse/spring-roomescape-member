package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.model.ReservationTime;

public record ReservationTimeRequest(
        @NotNull
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {
    public ReservationTime toEntity() {
        return new ReservationTime(
                null,
                startAt
                );
    }
}
