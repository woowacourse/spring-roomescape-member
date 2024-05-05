package roomescape.service.dto.request;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeSaveRequest(@NotNull LocalTime startAt) {

    public static ReservationTime toEntity(ReservationTimeSaveRequest request) {
        return new ReservationTime(request.startAt());
    }
}
