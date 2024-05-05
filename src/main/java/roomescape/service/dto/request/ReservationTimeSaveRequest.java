package roomescape.service.dto.request;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeSaveRequest(LocalTime startAt) {

    public static ReservationTime toEntity(ReservationTimeSaveRequest request) {
        return new ReservationTime(request.startAt());
    }
}
