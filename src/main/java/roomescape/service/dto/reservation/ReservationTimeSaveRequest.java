package roomescape.service.dto.reservation;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.reservation.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeSaveRequest(@NotNull(message = "예약 시간은 null일 수 없습니다.") LocalTime startAt) {

    public static ReservationTime toEntity(ReservationTimeSaveRequest request) {
        return new ReservationTime(request.startAt());
    }
}
