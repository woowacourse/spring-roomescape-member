package roomescape.presentation.dto.response;

import roomescape.business.model.entity.ReservationTime;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public record ReservationTimeResponse(
        String id,
        LocalTime startAt
) {
    public static List<ReservationTimeResponse> from(List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .sorted(Comparator.comparing(ReservationTimeResponse::startAt))
                .toList();
    }

    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.id(),
                reservationTime.startAt()
        );
    }
}
