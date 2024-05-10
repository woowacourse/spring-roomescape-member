package roomescape.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.reservation.ReservationTime;
import roomescape.service.dto.validation.TimeFormat;

import java.time.LocalTime;

public record ReservationTimeRequest(
        @NotBlank @TimeFormat String startAt
) {
    public ReservationTime toDomain() {
        return new ReservationTime(LocalTime.parse(startAt).withSecond(0));
    }
}
