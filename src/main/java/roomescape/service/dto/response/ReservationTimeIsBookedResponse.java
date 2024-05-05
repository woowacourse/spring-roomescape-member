package roomescape.service.dto.response;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public record ReservationTimeIsBookedResponse(LocalTime startAt, Long timeId, boolean alreadyBooked) {

    public ReservationTimeIsBookedResponse(ReservationTime reservationTime, boolean alreadyBooked) {
        this(reservationTime.getStartAt(), reservationTime.getId(), alreadyBooked);
    }

    public static List<ReservationTimeIsBookedResponse> listOf(Map<ReservationTime, Boolean> availability) {
        return availability.keySet().stream()
                .map(reservationTime -> new ReservationTimeIsBookedResponse(
                        reservationTime,
                        availability.get(reservationTime))
                ).toList();
    }
}
