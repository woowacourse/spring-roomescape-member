package roomescape.presentation.dto.response;

import roomescape.business.model.entity.ReservationTime;

import java.time.LocalTime;
import java.util.List;

public record ReservationTimeResponse(
        long id,
        LocalTime startAt
) {
    public static List<ReservationTimeResponse> from(List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }
}
