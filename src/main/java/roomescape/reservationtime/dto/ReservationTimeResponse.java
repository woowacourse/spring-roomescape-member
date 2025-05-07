package roomescape.reservationtime.dto;

import java.time.LocalTime;
import java.util.List;
import roomescape.reservationtime.domain.ReservationTime;

public record ReservationTimeResponse(Long id, LocalTime startAt) {

    public static ReservationTimeResponse from(ReservationTime time) {
        return new ReservationTimeResponse(time.getId(), time.getStartAt());
    }

    public static List<ReservationTimeResponse> from(List<ReservationTime> times) {
        return times.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }
}
