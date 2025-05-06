package roomescape.dto.reservationtime;

import java.time.LocalTime;
import java.util.List;
import roomescape.entity.ReservationTimeEntity;

public record ReservationTimeResponse(Long id, LocalTime startAt) {

    public static ReservationTimeResponse from(ReservationTimeEntity time) {
        return new ReservationTimeResponse(time.id(), time.startAt());
    }

    public static List<ReservationTimeResponse> from(List<ReservationTimeEntity> times) {
        return times.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }
}
