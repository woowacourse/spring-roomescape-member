package roomescape.controller.dto.response;

import java.time.LocalTime;
import java.util.List;
import roomescape.model.entity.ReservationTime;

public record ReservationTimeResponse(
        long id,
        LocalTime startAt
) {
    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }

    public static List<ReservationTimeResponse> from(List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }
}
