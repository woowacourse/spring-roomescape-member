package roomescape.presentation.dto.response;

import roomescape.business.model.entity.ReservationTime;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public record BookedReservationTimeResponse(
        String id,
        LocalTime startAt,
        boolean alreadyBooked
) {
    public static List<BookedReservationTimeResponse> from(Map<Boolean, List<ReservationTime>> reservationTimes) {
        return reservationTimes.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(rt -> new BookedReservationTimeResponse(
                                rt.id(),
                                rt.startAt(),
                                !entry.getKey()
                        ))
                )
                .sorted(Comparator.comparing(BookedReservationTimeResponse::startAt))
                .toList();

    }
}
