package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeRequest(@NotNull @NotBlank String startAt) {
    public ReservationTime toDomain() {
        LocalTime time = LocalTime.parse(startAt);
        return new ReservationTime(time);
    }
}
